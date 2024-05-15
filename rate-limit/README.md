## 算法

### 令牌桶算法
特点
- 瞬时QPS &gt; 设定QPS
- 平均QPS ≤ 设定QPS

变量（2个）
- 瞬时允许最大流量
- 令牌发放速度

与下游服务
- 修改令牌发放速度，可以配合下游服务扩缩容
- guava库的RateLimiter的内部实现，与Spring Cloud Gateway内置的RedisRateLimiter、组件ratelimiter-spring-boot-starter 这两者使用的redis-lua脚本实现逻辑，有以下区别
  - guava库的RateLimiter可以“赊账”，允许acquire()比capacity大的请求，将nextFreeTicketTime延后到未来的时间点上；redis-lua脚本则不允许

### 漏桶算法
特点
- 实际QPS ≤ 设定QPS

变量（2个）
- 桶大小=缓冲队列大小
- 放行速度

简单总结
- 削峰
- 任意速率进，固定速率出

### 滑动窗口计数器
变量（2个）
- 时间窗口长度
- 最大处理数量

总结
- 简单
- 存在边界问题，当窗口设置过大，突发流量集中在窗口边界前后时，造成实际效果仍大于限流的预设值
- 流量不均匀分布时，服务存在空转期

## 分布式限流

简单的应用侧业务限流，如验证码发送，可使用 [ratelimiter-spring-boot-starter](https://github.com/TapTap/ratelimiter-spring-boot-starter/tree/master)，该starter内部使用了redis+lua的方式实现令牌桶算法

Nginx+lua脚本（todo）

Spring Cloud Gateway限流（todo）
https://blog.csdn.net/netyeaxi/article/details/104382207

## 参考资料
有些博文的介绍和个人理解的不同，博文之间的描述也有些不相符合，以个人理解为准
[限流之令牌桶算法实现 - iMoe Tech](https://blog.imoe.tech/2021/11/20/46-limiter-ticket-bucket/)
[限流：计数器、漏桶、令牌桶 三大算法的原理与实战（史上最全） - 疯狂创客圈 - 博客园](https://www.cnblogs.com/crazymakercircle/p/15187184.html#autoid-h3-5-3-0)
[java - 接口限流算法：漏桶算法&令牌桶算法 - 架构师技术栈 - SegmentFault 思否](https://segmentfault.com/a/1190000015967922)
[高并发系统之限流特技 - start枫 - 博客园](https://www.cnblogs.com/cmfwm/p/8032994.html)
[架构之高并发：限流 | Java 全栈知识体系](https://pdai.tech/md/arch/arch-y-ratelimit.html#%E7%AE%97%E6%B3%95)