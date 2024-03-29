# qqplayer
## 系统设计与实现

### (1) 计算机体系设计
采用IDEA开发工具、 Scene Builder、MySQL、Navicat,在jdk-18版本下进行项目开发。
主要涉及技术：
__Javafx、数据库编程、多线程编程、文件处理、泛型编程与lambda表达式。__

### (2) 功能详细设计与数据库字典

1.	QQ播放器核心功能：音乐播放
点击播放按钮后开始在文件链表中播放歌曲文件，再次点击暂停播放。点击上一曲或下一曲后在暂停当前歌曲播放，清空当前歌曲，选择上一首或下一首播放。同时，可以通过拖动播放进度条来调整音乐播放进度。

2. 音乐文件导入与删除
使用文件选择器导入音乐文件，对文件名进行处理后显示在歌曲列表的界面。
如果进行相关操作时已经登录账号，在导入文件时使用迭代器将每个文件的本地路径名添加到数据库表中，删除文件时，从数据库中删除数据库中登录账号对应的该文件的路径名

3. 账号登录与注册
注册账号时，不允许账号和密码为空，同时限制最长账号为20位字符，在数据库中查询 `account` 表，返回账号对应的密码信息（如果该账号在数据库中存在的话，不存在就提示用户注册账号），比对返回信息和输入的密码
如果比对成功就显示登陆成功，否则提示登陆失败。
登录成功后，查询该账号对应的歌曲信息，如果数据库中存有相关信息，就按照返回的歌曲路径，从本地导入歌曲文件。

4. 主题切换功能
在主界面点击主题按钮，弹出上下文菜单，进行主题选择。选择主题后切换相应背景图片和颜色。


5. 数据库表设计如下：

账号密码表（num 为主键，num和password非空）
|num | password|
|---|---|
|NULL|NULL|

账号歌曲表(num 和 music非空)
|num | music|    
|---|---|
|NULL|NULL|



### 系统测试
> 1. 音乐播放、主界面显示、进度条、歌词显示、频谱显示功能：    
![img-20220622151626](https://user-images.githubusercontent.com/100141391/179643747-76cfc75d-f401-45d2-ac68-6b2517746312.png)


> 2、添加歌曲界面     
![img-20220622151650](https://user-images.githubusercontent.com/100141391/179643795-04252f86-3d09-4aea-9f18-2c0dea5f82a2.png)

> 3. 登录界面    
![img-20220622151708](https://user-images.githubusercontent.com/100141391/179643837-1f68435b-ecce-4ba4-b0b1-c6cb3ace33d0.png)

> 4. 切换主题功能    
![img-20220622151814](https://user-images.githubusercontent.com/100141391/179644080-f1fdc493-6fee-4965-aefd-f0b53c34a934.png)   
![img-20220622151817](https://user-images.githubusercontent.com/100141391/179644100-1158f227-16bd-4326-b516-065cbef323cc.png)



> 5. 音量滑块拖动   
![img-20220622151918](https://user-images.githubusercontent.com/100141391/179643968-c8490ad5-8f08-4596-88e9-a300f099c53e.png)


注：本项目主要参考： `https://github.com/leewyatt/simple-musicplayer`
