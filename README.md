# SureMap

一款以 WebView + Leaflet 为核心的轻量地图标注工具（Android）。
项目创意与需求由本人提出，代码由 AI 辅助开发完成。

## 功能特性

- 多底图切换（标准街道图 / 卫星影像）
- 在地图上**长按添加记录**（标题 + 内容），生成可点击的标记点
- 按区域 / 国家着色标注（黑 / 白 / 默认），用于专题地图标注
- 撤销 / 重做，多项目分组管理，导入 / 导出
- 内置世界、中国、日本、美国等行政区划 GeoJSON 底图

## 工程结构

```
SureMap/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/sure/mapnote/   # Android Java 层（Activity / 数据 / 存储）
│       ├── assets/
│       │   ├── map.html             # 地图界面核心（Leaflet + 交互逻辑）
│       │   ├── leaflet/             # Leaflet 地图库（第三方）
│       │   ├── regions/             # 行政区划 GeoJSON 数据
│       │   └── official.txt
│       └── res/                     # 图标 / 主题 / 字符串 / 颜色
├── build.gradle / settings.gradle   # Gradle 构建配置
└── README.md
```

完整的中英文目录结构说明见 [docs/project-structure.md](docs/project-structure.md)。

## 构建

环境要求：JDK 17+、Android SDK（platform 33）。

```bash
./gradlew assembleDebug
```

产物路径：`app/build/outputs/apk/debug/app-debug.apk`。
如需签名发布，请在 `app/build.gradle` 的 `signingConfigs` 中配置你的签名密钥。

## 技术说明

- 地图渲染与交互（长按添加记录、区域着色、标记显示）全部由 `assets/map.html` 实现，
  通过 `WebView` + `@JavascriptInterface` 与 Android 层通信（见 `MapActivity`）。
- 数据使用 JSON 存储（项目、标记点、区域状态），通过 `RepoStore` 落盘。
- **第三方库**：Leaflet 地图库（BSD-2-Clause），完整版权声明见 `assets/leaflet/`。

> 来源说明：Android Java 层代码由发布版 APK 反编译还原，用于保持仓库结构完整、可直接构建；
> 界面与逻辑的原始源码即 `assets/map.html`，为原始 HTML 文件。

## License

本项目基于 MIT License 开源，仅供学习交流使用。详见 [LICENSE](LICENSE)。

第三方组件 Leaflet 遵循其 BSD-2-Clause 许可证。
