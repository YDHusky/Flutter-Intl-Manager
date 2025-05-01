package online.yudream.intl.common;

import com.alibaba.fastjson2.JSONObject;
import com.intellij.openapi.project.Project;
import online.yudream.intl.entity.Config;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class L10nManager {
    private Project project;
    List<String> languages;
    Map<String, Map<String, String>> localizations;

    public L10nManager(Project project) {
        this.project = project;
        languages = new ArrayList<>();
        localizations = new HashMap<>();
        loadLanguages();
    }

    public void loadLanguages() {
        File dir = new File(project.getBasePath(), Config.getInstance().getL10nFolderPath());
        languages.clear();
        localizations.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String language = file.getName().split("intl_")[1].replaceAll("\\.arb", "");// 获取语言名称
                    languages.add(language);
                    loadArbFile(file, language);
                }
            }
        }

    }


    public void loadLanguage(String language) {
        File arbFile = getArbFile(language);
        if (arbFile.exists()) {
            loadArbFile(arbFile, language);
        }
    }

    private void loadArbFile(File file, String language) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            JSONObject data = JSONObject.parse(content);
            for (String key : data.keySet()) {
                String value = data.getString(key);
                if (!localizations.containsKey(key)) {
                    localizations.put(key, new HashMap<>());
                }
                localizations.get(key).put(language, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLanguage(String language, Map<String, String> localizations) {
        File file = getArbFile(language);
        if (Objects.equals(language, "")) {
            return;
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {

            }
        }
        String json = JSONObject.toJSONString(localizations);
        try {
            Files.write(file.toPath(), json.getBytes());
            loadLanguage(language);
        } catch (Exception e) {

        }
    }

    public void addValue(String key, Map<String, String> localization) {
        localizations.put(key, localization);
        Map<String, Map<String, String>> newLocalizations = new HashMap<>();
        for (String k : localizations.keySet()) {
            for (String lang : localizations.get(k).keySet()) {
                if (!newLocalizations.containsKey(lang)) {
                    newLocalizations.put(lang, new HashMap<>());
                }
                newLocalizations.get(lang).put(k, localizations.get(k).get(lang));
            }
        }

        saveAllLanguages(newLocalizations);
    }

    public void saveAllLanguages(Map<String, Map<String, String>> localizations) {
        for (String language : localizations.keySet()) {
            saveLanguage(language, localizations.get(language));
        }
    }

    public File getArbFile(String language) {
        String filename = "intl_" + language + ".arb";
        File dir = new File(project.getBasePath(), Config.getInstance().getL10nFolderPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, filename);
    }

    public Map<String, Map<String, String>> getLocalizations() {
        return localizations;
    }

    public void setLocalizations(Map<String, Map<String, String>> localizations) {
        this.localizations = localizations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    static private L10nManager instance;

    public static L10nManager getInstance(Project project) {
        if (instance == null) {
            instance = new L10nManager(project);
        }
        return instance;
    }

    public static L10nManager getInstance() {
        return instance;
    }
}
