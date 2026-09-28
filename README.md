# Android 虚拟定位应用 - 使用指南

## 项目结构
```
aa/
├── app/
│   ├── src/main/
│   │   ├── java/com/mocklocation/aa/
│   │   │   ├── MainActivity.kt          # 主界面
│   │   │   └── LocationService.kt       # 后台服务
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml    # UI 布局
│   │   │   └── values/
│   │   │       └── strings.xml          # 字符串资源
│   │   └── AndroidManifest.xml
│   ├── build.gradle                     # app 配置
│   └── proguard-rules.pro
├── build.gradle                         # 项目配置
└── settings.gradle
```

## 核心功能

### 1. 虚拟位置设置
- 在应用中输入**纬度**和**经度**
- 点击"设置位置"按钮
- 应用会通过系统的 LocationManager 注册虚拟位置提供者

### 2. 系统级集成
- 使用 Android 官方 API: `LocationManager.addTestProvider()`
- 注册虚拟 GPS 提供者到系统
- 所有其他应用通过系统 API 获取位置时，会自动获取虚拟位置

### 3. 后台服务
- 启动服务可保证虚拟位置持续生效
- 即使关闭应用，其他 app 仍能获取虚拟位置

## 编译与运行

### 前置条件
- 安装 Android Studio（版本 2021.1 及以上）
- Android SDK（API 21+）
- 连接真机或启动模拟器

### 编译步骤

1. **使用 Android Studio 打开项目**
   ```bash
   # 在 Android Studio 中 File > Open > 选择 aa 目录
   ```

2. **等待 Gradle 同步**
   - Android Studio 会自动下载依赖
   - 若出现依赖错误，点击"Sync Now"重试

3. **连接设备**
   - 真机：USB 连接，启用开发者模式和 USB 调试
   - 模拟器：在 Android Studio 中打开 AVD Manager 创建模拟器

4. **运行应用**
   - 点击 Run > Run 'app'
   - 或按快捷键 Shift + F10

## 应用内操作

### 设置虚拟位置
1. 输入纬度、经度、精度（默认北京：39.90469, 116.40516）
2. 点击"设置位置"按钮
3. 显示"位置已设置"提示

### 启动后台服务
- 点击"启动服务"保证虚拟位置持续有效
- 点击"停止服务"关闭虚拟定位

## 验证虚拟位置生效

1. **设置为系统模拟定位应用**
   ```
   开发者选项 > 选择模拟位置应用 > 选择"虚拟定位"
   ```

2. **测试其他应用**
   - 打开 Google Maps / 百度地图等应用
   - 应该显示你设置的虚拟位置
   - 任何基于 GPS 的应用都会读取虚拟位置

## 常见问题

### Q: 其他应用无法读取虚拟位置
**A:** 确保：
1. 已在开发者选项中选择此应用为模拟定位应用
2. 已点击"启动服务"按钮
3. 应用具有 ACCESS_MOCK_LOCATION 权限

### Q: 精度参数的作用
**A:** 设置 GPS 信号的精度范围（单位米），精度越小越逼真

### Q: 可以设置动态移动轨迹吗
**A:** 当前版本只支持静态位置，可在 LocationService 中添加 Timer 定期更新位置

## 技术细节

### 使用的 Android API
- `LocationManager.addTestProvider()` - 注册虚拟定位提供者
- `LocationManager.setTestProviderLocation()` - 设置虚拟位置
- `LocationManager.setTestProviderEnabled()` - 启用/禁用提供者

### 权限声明（AndroidManifest.xml）
- `ACCESS_FINE_LOCATION` - 精确位置
- `ACCESS_COARSE_LOCATION` - 粗略位置
- `ACCESS_MOCK_LOCATION` - 虚拟定位

### 服务后台保活
- 使用 `startForeground()` 显示通知
- 防止系统杀死应用进程

## 后续扩展

如需添加以下功能，可修改代码：
1. **地图选择位置** - 集成高德地图 SDK
2. **轨迹回放** - 在 LocationService 中实现定时更新
3. **多点模拟** - 支持设置多个虚拟位置点
4. **数据导入** - 从文件导入 GPS 轨迹
