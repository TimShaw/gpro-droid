/******************************************************************************
Copyright (C), 2008-2018, HUNDSUN Tech. Co., Ltd.
All rights reserved. 
******************************************************************************/

#ifndef _COM_TYPE_H
#define _COM_TYPE_H

#include <sys/types.h>

#include <map>
#include <list>
#include <vector>
using namespace std;
#define _USE_32BIT_TIME_T
#include <sys/time.h>

/** 
*  @defgroup COMFUNCLIB 公共模块
*  @brief 本模块用于提供系统的一致化环境定义与共用功能。
*  @author  朱伟
*  @version 1.0
*  @date    2009-04-8 11:06:29
*  @{
*/

/** @name 基本类型定义
*@{
*/

typedef unsigned char  U8;
typedef unsigned short U16;
typedef unsigned int   U32;
typedef u_int64_t      U64;
typedef int            S32;
typedef short          S16;

/** @}基本类型定义*/
#ifndef PATH_MAX
#define PATH_MAX 1024
#endif



#define MAX_THREAD_PRIORITY 5

#define EVENT_SRV_OUT_TIME			7000000
#define EVENT_SRV_SOCKET_OUT_TIME	8000000
#define EVENT_DEV_OUT_TIME			10000000


/** @name 常用类型定义
*@{
*/
/**安全内存块类型*/ 
typedef struct save_buf /* SBUF - save_buf */ 
{
	U8*   pbuf;
	U32   len;
}SBUF,*LPSBUF; /**<安全内存块类型*/ 



/**内存链表类型*/ 
typedef list<LPSBUF> SBUF_LIST,*LPSBUF_LIST;

 
/** @}常用类型定义*/

/** @}公共模块*/
#define MOVEBIT    10  //字位移

#define DVIT_DEBUG 

//#define TEST_DATALINK   //测试数据链路层传输能力
//#define TEST_EVENT      //测试信令传输并发能力
//#define TEST_PERF       //测试单数据传输时间
//#define STREAM_CHECK      //流数据检查
#define NO_MEM_POOL       //不使用内存池技术

#define PRESSURE_TEST     //压力测试
/**压力测试使用的tid倍数*/
#define PRESS_TEST_NUM       10000

#define MEMWATCH
#define MW_STDIO


#endif /*_COM_TYPE_H*/






