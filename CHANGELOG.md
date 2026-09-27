# Changelog

## [1.0.1] - 2026-09-27

### Fixed
- **长按添加记录弹出两次**：Android WebView 长按会同时触发自定义 `touchstart` 定时器与原生 `contextmenu`，
  导致记录弹窗弹出两次。已对 `attachLongPress()` 增加去重逻辑（1 秒内、坐标相同只上报一次），见 `assets/map.html`。
- **添加记录不显示在地图上**：`markerLayer` 原用 `L.layerGroup()` 创建，却调用了其不具备的 `bringToFront()`，
  运行时抛 `TypeError` 使 `renderMarkers()` 无法执行，标记永不渲染。已改为 `L.featureGroup()`（两处）。

### Changed
- 包名由 `com.kaifa.mapnote` 改为 `com.sure.mapnote`（应用标识 + 内部类名 + 资源包名已全部同步）。

## [1.0.0]
- 首个可用版本：多底图切换、长按添加记录、区域着色、项目分组管理、导入导出。
