package com.sure.mapnote;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class Lang {
    public static final String EN = "en";
    public static final String JA = "ja";
    private static final Map<String, String[]> M = new LinkedHashMap();
    public static final String ZH = "zh";
    private static String lang;

    static {
        put("app_name", "SureMap", "SureMap", "SureMap");
        put("lang_label", "语言", "Language", "言語");
        put("lang_zh", "简体中文", "Chinese (Simplified)", "簡体中国語");
        put("lang_en", "English", "English", "英語");
        put("lang_ja", "日本語", "Japanese", "日本語");
        put("home_sub", "记录与观察世界", "Record & Observe the World", "世界を記録・観察");
        put("create_new", "创建新项目", "Create New Project", "新規プロジェクト作成");
        put("repository", "仓库", "Repository", "倉庫");
        put("import_export", "导入/导出", "Import / Export", "インポート/エクスポート");
        put("official", "官方介绍", "Official Introduction", "公式紹介");
        put("title_create", "创建新项目", "Create New Project", "新規プロジェクト作成");
        put("project_name", "项目名称", "Project Name", "プロジェクト名");
        put("name_hint", "请输入项目名称", "Enter project name", "プロジェクト名を入力");
        put("map_mode", "地图模式", "Map Mode", "地図モード");
        put("world_map", "世界地图", "World Map", "世界地図");
        put("region_map", "地区地图（选择素材）", "Region Map (pick material)", "地域地図（素材を選択）");
        put("pick_material", "选择素材", "Pick Material", "素材を選択");
        put("pick_map", "选择地图", "Pick Map", "地図を選択");
        put("no_material", "暂无素材，请先在导入/导出中导入地理素材", "No material yet. Import a GeoJSON material first.", "素材がありません。先に素材をインポートしてください");
        put("create_btn", "创建", "Create", "作成");
        put("name_required", "请输入项目名称", "Please enter a project name", "プロジェクト名を入力してください");
        put("tool_record", "记录", "Record", "記録");
        put("tool_area", "区域选择", "Area Select", "エリア選択");
        put("undo", "撤回", "Undo", "元に戻す");
        put("redo", "恢复", "Redo", "やり直し");
        put("map_standard", "标准地图", "Standard Map", "標準地図");
        put("map_satellite", "卫星图", "Satellite", "衛星写真");
        put("save", "保存", "Save", "保存");
        put("saved", "已保存到仓库", "Saved to repository", "倉庫に保存しました");
        put("select_group", "选择分组", "Select Group", "グループ選択");
        put("confirm", "确定", "OK", "OK");
        put("cancel", "取消", "Cancel", "キャンセル");
        put("delete", "删除", "Delete", "削除");
        put("edit", "编辑", "Edit", "編集");
        put("new_project", "未命名项目", "Untitled Project", "無題プロジェクト");
        put("area_black", "拉黑地区", "Blacklist Regions", "ブラックリスト");
        put("area_white", "白名单地区", "Whitelist Regions", "ホワイトリスト");
        put("area_mode_title", "区域选择模式", "Area Select Mode", "エリア選択モード");
        put("record_title", "记录内容", "Record Note", "記録内容");
        put("marker_title", "标记标题", "Marker Title", "マーカー名");
        put("marker_content", "记录内容", "Note Content", "記録内容");
        put("marker_content_hint", "写下你想记录的内容...", "Write what you want to record...", "記録したい内容を入力...");
        put("add_record", "添加记录", "Add Record", "記録を追加");
        put("record_here", "在此位置添加记录？", "Add a record at this location?", "この位置に記録を追加しますか？");
        put("marker_options", "标记操作", "Marker Options", "マーカー操作");
        put("confirm_delete_marker", "确定删除该标记？", "Delete this marker?", "このマーカーを削除しますか？");
        put("title_repo", "仓库", "Repository", "倉庫");
        put("add_group", "添加分组", "Add Group", "グループ追加");
        put("search_hint", "搜索项目或素材名称...", "Search projects or materials...", "プロジェクト/素材を検索...");
        put("group_name", "分组名称", "Group Name", "グループ名");
        put("group_name_hint", "请输入分组名称", "Enter group name", "グループ名を入力");
        put("material_group", RepoStore.DEFAULT_GROUP, "Materials", RepoStore.DEFAULT_GROUP);
        put("no_entries", "暂无内容", "No items", "項目がありません");
        put("open", "打开", "Open", "開く");
        put("rename", "重命名", "Rename", "名前を変更");
        put("move", "移动到分组", "Move to Group", "グループへ移動");
        put("delete_entry", "删除", "Delete", "削除");
        put("confirm_delete_entry", "确定删除？此操作不可恢复", "Delete? This cannot be undone.", "削除しますか？元に戻せません");
        put("new_name", "新名称", "New Name", "新しい名前");
        put("select_group_move", "选择目标分组", "Select target group", "移動先グループを選択");
        put("group_exists", "分组已存在", "Group already exists", "グループは既に存在します");
        put("entry_type_project", "项目", "Project", "プロジェクト");
        put("entry_type_material", RepoStore.DEFAULT_GROUP, "Material", RepoStore.DEFAULT_GROUP);
        put("title_ie", "导入/导出", "Import / Export", "インポート/エクスポート");
        put("import_tab", "导入", "Import", "インポート");
        put("export_tab", "导出", "Export", "エクスポート");
        put("import_project_btn", "导入项目文件", "Import Project File", "プロジェクトをインポート");
        put("import_material_btn", "导入素材/文档", "Import Material / Document", "素材/ドキュメントをインポート");
        put("import_hint", "支持导入项目文件(.json)与地理素材文档(.geojson/.json)", "Supports project files (.json) and GeoJSON materials.", "プロジェクト(.json)と素材(.geojson/.json)に対応");
        put("export_hint", "勾选要导出的仓库项目", "Select repository projects to export", "エクスポートするプロジェクトを選択");
        put("export_btn", "导出所选", "Export Selected", "選択をエクスポート");
        put("export_ok", "导出成功", "Exported", "エクスポートしました");
        put("export_none", "请至少选择一个项目", "Please select at least one project", "プロジェクトを選択してください");
        put("import_ok", "导入成功", "Imported", "インポートしました");
        put("import_fail", "导入失败：文件格式不正确", "Import failed: invalid format", "インポート失敗：形式が正しくありません");
        put("no_projects_in_repo", "仓库暂无项目", "No projects in repository", "倉庫にプロジェクトがありません");
        put("title_official", "官方介绍", "Official Introduction", "公式紹介");
        put("official_path", "配置文件路径（可用 MT 管理器修改）", "Config file (editable via MT Manager)", "設定ファイル（MTマネージャーで編集可）");
        put("reload", "重新加载", "Reload", "再読み込み");
        put("back", "返回", "Back", "戻る");
        put("unsaved_msg", "有未保存的修改，是否保存？", "Unsaved changes. Save now?", "未保存の変更があります。保存しますか？");
        put("discard", "放弃", "Discard", "破棄");
        lang = ZH;
    }

    private static void put(String str, String str2, String str3, String str4) {
        M.put(str, new String[]{str2, str3, str4});
    }

    public static void setLang(String str) {
        if (EN.equals(str) || JA.equals(str) || ZH.equals(str)) {
            lang = str;
        } else {
            lang = ZH;
        }
    }

    public static String getLang() {
        return lang;
    }

    public static String get(String str) {
        String[] strArr = M.get(str);
        if (strArr == null) {
            return str;
        }
        return strArr[ZH.equals(lang) ? (char) 0 : EN.equals(lang) ? (char) 1 : (char) 2];
    }

    public static String langName(String str) {
        return EN.equals(str) ? "English" : JA.equals(str) ? "日本語" : "简体中文";
    }
}
