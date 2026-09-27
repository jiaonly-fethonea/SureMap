SureMap
一款以 WebView + Leaflet 为核心的轻量地图标注工具（Android）。 项目创意与需求由本人提出，代码由 AI 辅助开发完成。

A lightweight map annotation tool for Android, built around WebView + Leaflet. The project idea and requirements were proposed by the author; the code was developed with AI assistance.

功能特性 / Features
多底图切换（标准街道图 / 卫星影像）
Multiple base map layers (standard street map / satellite imagery)
在地图上长按添加记录（标题 + 内容），生成可点击的标记点
Long-press on the map to add records (title + content), creating clickable markers
按区域 / 国家着色标注（黑 / 白 / 默认），用于专题地图标注
Color-code regions / countries (black / white / default) for thematic map annotation
撤销 / 重做，多项目分组管理，导入 / 导出
Undo / redo, multi-project group management, import / export
内置世界、中国、日本、美国等行政区划 GeoJSON 底图
Built-in administrative GeoJSON basemaps for the World, China, Japan, the United States, and more
工程结构 / Project Structure


SureMap/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/sure/mapnote/   # Android Java 层（Activity / 数据 / 存储）
│       │                              # Android Java layer (Activity / data / storage)
│       ├── assets/
│       │   ├── map.html             # 地图界面核心（Leaflet + 交互逻辑）
│       │                              # Core map UI (Leaflet + interaction logic)
│       │   ├── leaflet/             # Leaflet 地图库（第三方）
│       │                              # Leaflet map library (third-party)
│       │   ├── regions/             # 行政区划 GeoJSON 数据
│       │                              # Administrative division GeoJSON data
│       │   └── official.txt
│       └── res/                     # 图标 / 主题 / 字符串 / 颜色
│                                      # Icons / themes / strings / colors
├── build.gradle / settings.gradle   # Gradle 构建配置
│                                      # Gradle build configuration
└── README.md


构建 / Build
环境要求：JDK 17+、Android SDK（platform 33）。

Requirements: JDK 17+, Android SDK (platform 33).

./gradlew assembleDebug
产物路径：app/build/outputs/apk/debug/app-debug.apk。

Output path: app/build/outputs/apk/debug/app-debug.apk.

如需签名发布，请在 app/build.gradle 的 signingConfigs 中配置你的签名密钥。

For a signed release build, configure your signing key in signingConfigs within app/build.gradle.

技术说明 / Technical Notes
地图渲染与交互（长按添加记录、区域着色、标记显示）全部由 assets/map.html 实现， 通过 WebView + @JavascriptInterface 与 Android 层通信（见 MapActivity）。
Map rendering and interaction (long-press to add records, region coloring, marker display) are all implemented in assets/map.html, communicating with the Android layer via WebView + @JavascriptInterface (see MapActivity).
数据使用 JSON 存储（项目、标记点、区域状态），通过 RepoStore 落盘。
Data is stored in JSON (projects, markers, region states) and persisted via RepoStore.
第三方库：Leaflet 地图库（BSD-2-Clause），完整版权声明见 assets/leaflet/。
Third-party library: Leaflet map library (BSD-2-Clause). Full copyright notice can be found in assets/leaflet/.
来源说明：Android Java 层代码由发布版 APK 反编译还原，用于保持仓库结构完整、可直接构建； 界面与逻辑的原始源码即 assets/map.html，为原始 HTML 文件。

Source note: The Android Java layer code was recovered by decompiling the released APK to keep the repository structure complete and directly buildable. The original source for the UI and logic is assets/map.html, which is the original HTML file.

License / 许可证
本项目基于 MIT License 开源，仅供学习交流使用。详见 LICENSE。 第三方组件 Leaflet 遵循其 BSD-2-Clause 许可证。

This project is open-sourced under the MIT License for learning and exchange purposes only. See LICENSE for details. The third-party component Leaflet follows its BSD-2-Clause license.