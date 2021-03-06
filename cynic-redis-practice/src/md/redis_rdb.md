###redis ###

####RDB ####

redis在指定的情况下会触发快照

1.自己配置的快照规则
    
     SNAPSHOTTING
    
     把数据库存到磁盘上:
    
      save <seconds> <changes>
    
      会在指定秒数和数据变化次数之后把数据库写到磁盘上。
    
      下面的例子将会进行把数据写入磁盘的操作:
      900秒（15分钟）之后，且至少1次变更
      300秒（5分钟）之后，且至少10次变更
      60秒之后，且至少10000次变更
    
      注意：你要想不写磁盘的话就把所有 "save" 设置注释掉就行了。
    
      通过添加一条带空字符串参数的save指令也能移除之前所有配置的save指令
      像下面的例子：
          
        save ""
        save 900 1
        save 300 10
        save 60 10000
        
        
2.手动执行save或bgsave

        save:执行内存的数据同步到磁盘的操作,这个操作会阻塞客户端的请求;
        bgsave: backgroundsave,在后台异步执行快照操作,不会阻塞客户端的请求;
        
3.执行flushall的时候
        
        清除内存的所有数据,只要快照的规则不为空,即 (1) 的规则存在,redis会执行快照;
        
4.执行复制的时候

         
#### 快照的实现原理 ####

    redis会使用fork函数复制一份当前进程的副本(子线程),fork进程负责把内存的数据同步到内存的临时文件,父进程继续处理客户端请求;
         
         
#### redis RDB的优缺点 ####

    1.可能会存在数据丢失的情况,执行快照和下一次快照中间内存中的数据可能会丢失;
    2.可以最大化redis的性能;但如果内存中数据量过大,fork可能会很耗时;
    