package com.example.seckilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckilldemo.config.AccessLimit;
import com.example.seckilldemo.entity.TOrder;
import com.example.seckilldemo.entity.TSeckillOrder;
import com.example.seckilldemo.entity.TUser;
import com.example.seckilldemo.exception.GlobalException;
import com.example.seckilldemo.rabbitmq.MQSender;
import com.example.seckilldemo.service.ITGoodsService;
import com.example.seckilldemo.service.ITOrderService;
import com.example.seckilldemo.service.ITSeckillOrderService;
import com.example.seckilldemo.utils.JsonUtil;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.RespBean;
import com.example.seckilldemo.vo.RespBeanEnum;
import com.example.seckilldemo.vo.SeckillMessage;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
@Api(value = "秒杀", tags = "秒杀")
public class SeKillController implements InitializingBean {

    @Resource
    private ITGoodsService itGoodsService;
    @Resource
    private ITSeckillOrderService itSeckillOrderService;
    @Resource
    private ITOrderService orderService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MQSender mqSender;
    @Resource
    private RedisScript<Long> redisScript;

    private final Map<Long, Boolean> EmptyStockMap = new HashMap<>();


    @ApiOperation("获取验证码")
    @GetMapping(value = "/captcha")
    public void verifyCode(TUser tUser, Long goodsId, HttpServletResponse response) {
        if (tUser == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置请求头为输出图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + tUser.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败 " + e.getMessage());
        }
    }

    /**
     * 获取秒杀地址
     **/
    @ApiOperation("获取秒杀地址")
    @AccessLimit(second = 5, maxCount = 5)
    @GetMapping(value = "/path")
    @ResponseBody
    public RespBean getPath(TUser tuser, Long goodsId, String captcha) {
        if (tuser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        boolean check = orderService.checkCaptcha(tuser, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(tuser, goodsId);
        return RespBean.success(str);
    }


    /**
     * 获取秒杀结果
     *
     * @return orderId 成功 ；-1 秒杀失败 ；0 排队中
     * @author LiChao
     **/
    @ApiOperation("获取秒杀结果")
    @GetMapping("getResult")
    @ResponseBody
    public RespBean getResult(TUser tUser, Long goodsId) {
        if (tUser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = itSeckillOrderService.getResult(tUser, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 秒杀功能
     *
     * @return java.lang.String
     **/
    @ApiOperation("秒杀功能")
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, TUser user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //优化后代码
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //判断是否重复抢购
        TSeckillOrder tSeckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (tSeckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记，减少Redis的访问
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        try {
            Long stock = redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
            if (stock != null && stock < 0) {
                EmptyStockMap.put(goodsId, true);
                valueOperations.increment("seckillGoods:" + goodsId);
                return RespBean.error(RespBeanEnum.EMPTY_STOCK);
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("redis中查询不到key为stock的value值");
        }
        SeckillMessage seckillMessag = new SeckillMessage(user, goodsId);
        // 因为mq是异步执行的，所以请求会立马返回，达到异步削峰的效果
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessag));
        return RespBean.success(0);

    }

    /**
     * 秒杀功能-废弃
     **/
    @ApiOperation("秒杀功能-废弃")
    @RequestMapping(value = "/doSeckill2", method = RequestMethod.POST)
    public String doSecKill2(Model model, TUser user, Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = itGoodsService.findGoodsVobyGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复抢购
        TSeckillOrder seckillOrder = itSeckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        TOrder tOrder = orderService.secKill(user, goodsVo);
        model.addAttribute("order", tOrder);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }

    /**
     * 系统初始化，把商品库存数量加载到Redis
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> list = itGoodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
