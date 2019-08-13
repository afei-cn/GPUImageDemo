# GPUImageDemo

## 一、应用截图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190812184743393.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2FmZWlfXw==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190812184808331.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2FmZWlfXw==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190812184830104.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2FmZWlfXw==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190812184843212.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2FmZWlfXw==,size_16,color_FFFFFF,t_70)

## 二、前言
GPUImage 是一个开源的图像渲染的库，使用它可以轻松实现很多滤镜效果，也可以很轻松的定义和实现自己特有的滤镜效果。

地址：[https://github.com/cats-oss/android-gpuimage](https://github.com/cats-oss/android-gpuimage)

## 三、依赖工程
要想使用 GPUImage，使用 Android Studio 只需要在 build.gradle 里面添加相关的依赖即可。
```
implementation 'jp.co.cyberagent.android:gpuimage:2.0.3'
```

## 四、GPUImage 类介绍
### 1. 目录结构

|--- filter ： 这个包下面是各种滤镜效果类。  
|--- util ： 这个包下面是一些工具类。  
|--- GLTextureView ： 继承自 TextureView，和 GLSurfaceView 功能类似。  
|--- GPUImage ： 核心实现类，配合 GLSurfaceView/GLTextureView 和 GPUImageFilter 实现渲染。  
|--- GPUImageNativeLibrary ： 包含一些图片转码的 native 方法。  
|--- GPUImageRenderer ： 实际的渲染者。  
|--- GPUImageView ： 继承自 FrameLayout，封装了一个 GPUImage 和 GPUImageFilter，使用起来更方便。
