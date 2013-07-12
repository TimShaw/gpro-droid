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
*  @defgroup COMFUNCLIB ����ģ��
*  @brief ��ģ�������ṩϵͳ��һ�»����������빲�ù��ܡ�
*  @author  ��ΰ
*  @version 1.0
*  @date    2009-04-8 11:06:29
*  @{
*/

/** @name �������Ͷ���
*@{
*/

typedef unsigned char  U8;
typedef unsigned short U16;
typedef unsigned int   U32;
typedef u_int64_t      U64;
typedef int            S32;
typedef short          S16;

/** @}�������Ͷ���*/
#ifndef PATH_MAX
#define PATH_MAX 1024
#endif



#define MAX_THREAD_PRIORITY 5

#define EVENT_SRV_OUT_TIME			7000000
#define EVENT_SRV_SOCKET_OUT_TIME	8000000
#define EVENT_DEV_OUT_TIME			10000000


/** @name �������Ͷ���
*@{
*/
/**��ȫ�ڴ������*/ 
typedef struct save_buf /* SBUF - save_buf */ 
{
	U8*   pbuf;
	U32   len;
}SBUF,*LPSBUF; /**<��ȫ�ڴ������*/ 



/**�ڴ���������*/ 
typedef list<LPSBUF> SBUF_LIST,*LPSBUF_LIST;

 
/** @}�������Ͷ���*/

/** @}����ģ��*/
#define MOVEBIT    10  //��λ��

#define DVIT_DEBUG 

//#define TEST_DATALINK   //����������·�㴫������
//#define TEST_EVENT      //��������䲢������
//#define TEST_PERF       //���Ե����ݴ���ʱ��
//#define STREAM_CHECK      //�����ݼ��
#define NO_MEM_POOL       //��ʹ���ڴ�ؼ���

#define PRESSURE_TEST     //ѹ������
/**ѹ������ʹ�õ�tid����*/
#define PRESS_TEST_NUM       10000

#define MEMWATCH
#define MW_STDIO


#endif /*_COM_TYPE_H*/






