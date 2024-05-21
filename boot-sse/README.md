# 关于SSE

## 什么是SSE
SSE = Server Sent Events 服务端发送事件

## SSE与轮询
轮询：客户端发起多次请求，建立一次或多次连接；客户端为主动方；新消息(事件)产生后，只有客户端发起新请求才能获得，服务端无法主动推送到客户端
SSE：客户端发起一次请求，连接保持开启状态；新消息(事件)产生后，服务端可主动将其推送给客户端

## SSE与Websocket
SSE
- 单通道（单向），仅允许服务端将消息事件推送给客户端；
- 基于HTTP协议
- 使用文本格式传输数据
- 支持自动断线重连

Websocket
- 全双工，客户端与服务端均可主动发送消息给另一方；
- 基于WSS协议
- 使用二进制格式传输数据
- 需要自行实现断线重连

## 手动实现一个SSE
1. 返回数据 ≠ 结束请求流程，避免断开链接

## SSE规范
1. 基于HTTP1.1，请求头增加Connection: keep-alive
2. 请求头内容类型(RequestHeader Content-Type)需要设置为流类型：Content-Type: text/event-stream\[:charset=UTF-8\]
   1. text = 使用文本格式传输数据
   2. event-stream = 事件流类型
3. 数据格式
   message的格式为：`${field}:${value}\n\n`
   field有五种可能值
   - 空串: 心跳
   - data: 传输的数据
   - event: 事件（field的默认值）
   - id: 数据标识符，每一条传输的数据编号
   - retry: 重连时间

## 参考资料
- [【SpringBoot WEB 系列】SSE 服务器发送事件详解](https://cloud.tencent.com/developer/article/1620176)
