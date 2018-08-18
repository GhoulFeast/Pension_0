package com.overwork.pension.other

val BASEURL = ""//基本

//val LOGIN = "/denglu.action?"//登陆
//val MSGLIST = "/xxlb.action?"//消息列表
//val T_TASK = "/jrrw.action?"//今天任务
//val T_ABNORMAL = "/jbycxx.action?"//交班——异常信息
//val T_HANDOVERINFO = "/jbnrlb.action?"//交班——交班信息
//val TOMORROW_TASK = "/mrrw.action?"//明日任务
//val ABNORMALITY = "/scyc.action?"//异常情况提交
//val THIS_TIME_TASK = "/hlnr.action?"//护理内容
//val T_HANDOVERDIRECTOR = "/jbzghz.action?"//交班-主管
//val AUTO_UPDATE_MSG="/ifyxiaoxi.action?"//新消息
//val MSGLIST_READ = "/ydxx.action?"//消息——读
//val OLDMAN_INFO="/lrxx.action?"//老人信息
//val ROOM_LIST = "/fjlb.action?"//房间列表
//val OVER_EX = "/wczhrwx.action?"//完成任务详情测量项
//val OVER_TASK = "/wchlx.action?"//完成护理项
//val UP_FILE="/scfj.action?"//上传文件
//val DELET_FILE="/deleteFj.action?"//删除文件
//val IS_HANDOVER="/ifjbwc.action?"//是否交班成功

val LOGIN = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/login.json"//登陆
val MSGLIST = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/nsgList.json"//消息列表
val T_TASK = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/todayTask.json"//今天任务
val T_ABNORMAL = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/abnormalShift.json"//交班——异常信息https://github.com/wwan12/AnalogInterface/blob/master/abnormalShift.json
val T_HANDOVERINFO = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/shiftContent.json"//交班——交班信息https://github.com/wwan12/AnalogInterface/blob/master/shiftContent.json
val OVER_TASK = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/completeTheNursing.json"//完成护理项https://github.com/wwan12/AnalogInterface/blob/master/completeTheNursing.json
val TOMORROW_TASK = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/tomorrowTask.json"//明日任务https://github.com/wwan12/AnalogInterface/blob/master/tomorrowTask.json
val ABNORMALITY = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/completeTheNursing.json"//异常情况提交
val THIS_TIME_TASK = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/nursingContent.json"//护理内容
val T_HANDOVERDIRECTOR = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/supervisorSummary.json"//交班-主管https://github.com/wwan12/AnalogInterface/blob/master/supervisorSummary.json
val AUTO_UPDATE_MSG = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/isNewMsg.json"//新消息https://github.com/wwan12/AnalogInterface/blob/master/isNewMsg.json
val MSGLIST_READ = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/completeTheNursing.json"//消息——读
val IS_HANDOVER = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/ishandover.json"//是否交班成功
val ROOM_LIST = "https://raw.githubusercontent.com/wwan12/AnalogInterface/master/RoomList.json"//房间列表
val OLDMAN_INFO = "https://raw.githubusercontent.com/GhoulFeast/AnalogInterface/master/InformationOfTheOldMan.json"//老人信息https://github.com/wwan12/AnalogInterface/blob/master/InformationOfTheOldMan.json
val OVER_EX = ""//完成任务详情测量项
val UP_FILE=""//上传文件
val DELET_FILE=""//删除文件


//附件信息	http://localhost:8081/yly/appall/fjxx.action?hlrwId=4&czlx=01&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c
//保存常规记录	http://localhost:8081/yly/appall/savecgjl.action?后面是参数


//完成照护任务（工作内容）小项	http://localhost:8081/yly/appall/wczhrwx.action?zhrwId=1&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//登录	http://localhost:8081/yly/appall/denglu.action?userAccount=hz1&userPassword=123456

//今日任务	http://localhost:8081/yly/appall/jrrw.action?后面是参数
//点击开始工作（主管和其他吧的人员）	http://localhost:8081/yly/appall/jrrw.action?userId=02aa98d7-8669-422f-9f78-a1df8ded7925&kssj=7:00
//点击开始工作(是白班的护工）	http://localhost:8081/yly/appall/jrrw.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&kssj=7:00

//明日任务	http://localhost:8081/yly/appall/mrrw.action?后面是参数
//白班	http://localhost:8081/yly/appall/mrrw.action?userId=02aa98d7-8669-422f-9f78-a1df8ded7925
//主管和其他班次	http://localhost:8081/yly/appall/mrrw.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//房间列表	http://localhost:8081/yly/appall/fjlb.action?后面是参数
//http://localhost:8081/yly/appall/fjlb.action?userId=02aa98d7-8669-422f-9f78-a1df8ded7925

//护理内容	http://localhost:8081/yly/appall/hlnr.action?后面是参数
//http://localhost:8081/yly/appall/hlnr.action?hlrwId=4&czlx=01&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//完成护理项	http://localhost:8081/yly/appall/wchlx.action?后面是参数
//http://localhost:8081/yly/appall/wchlx.action?hlrwId=1&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//上传异常（保存护理内容）护工	http://localhost:8081/yly/appall/scyc.action?hlrwId=1&czlx=01&zbid=1&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&images=a.jsp,b.jsp&sounds=a.yp,b.yp&twsjz=tw&xysjz=80&mbsjz=90&rlsjz=89&clsjz=32&clbz=1&abnormal=需关注&abnormalType=01
//护工交接班	http://localhost:8081/yly/appall/scyc.action?czlx=02&zbid=1&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&images=d.jsp,e.jsp&sounds=f.yp,m.yp&abnormal=需关注&abnormalType=01
//主管交接班	http://localhost:8081/yly/appall/scyc.action?czlx=03&zbid=1&userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&images=d.jsp,e.jsp&sounds=f.yp,m.yp&abnormal=需关注&abnormalType=01

//老人信息	http://localhost:8081/yly/appall/lrxx.action?后面是参数
//http://localhost:8081/yly/appall/lrxx.action?lrid=2094abcaab154c6fabae2c00e13769a8

//消息列表	http://localhost:8081/yly/appall/xxlb.action?后面是参数
//发给“白班的人员”，当天的消息	http://localhost:8081/yly/appall/xxlb.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//交班—异常信息	http://localhost:8081/yly/appall/jbycxx.action?后面是参数
//汇总展示	http://localhost:8081/yly/appall/jbycxx.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//交班-内容列表	http://localhost:8081/yly/appall/jbnrlb.action?后面是参数
//复查护理记录	http://localhost:8081/yly/appall/jbnrlb.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//交班-主管汇总	http://localhost:8081/yly/appall/jbzghz.action?后面是参数
//http://localhost:8081/yly/appall/jbzghz.action?userId=f3bc98ad-6bac-4bad-b59d-438724916f1f

//是否交班完成	http://localhost:8081/yly/appall/ifjbwc.action?后面是参数
//http://localhost:8081/yly/appall/ifjbwc.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&jbrId=f3bc98ad-6bac-4bad-b59d-438724916f1f

//是否有新消息	http://localhost:8081/yly/appall/ifyxiaoxi.action?后面是参数
//http://localhost:8081/yly/appall/ifyxiaoxi.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c

//已读消息	http://localhost:8081/yly/appall/ydxx.action?后面是参数
//http://localhost:8081/yly/appall/ydxx.action?userId=1&messageId=1&type=1&zbpkid=3
//"http://localhost:8081/yly/appall/ydxx.action?userId=1&messageId=1&type=0&zbpkid=3
//分护理信息和通知信息"

//文件上传	http://localhost:8081/yly/appall/scfj.action
//http://localhost:8081/yly/test_wjsc.jsp
//"form 表单提交 加参数wjlx:文件类型0图片1声音；文件 file
//<form action=""${basepath}appall/scfj.action"" name=""wcwj_form""id=""wcwj_form"" method=""post"" enctype=""multipart/form-data"">"
//删除上传文件	http://localhost:8081/yly/appall/deleteFj.action?userId=ba1a6c53-cdf1-48d9-80cb-5d97307e038c&fb1pkid=e957e6579b134b29be89dfbbefca87d9

























