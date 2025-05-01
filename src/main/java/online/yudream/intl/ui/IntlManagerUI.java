package online.yudream.intl.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import online.yudream.intl.common.L10nManager;
import org.jdesktop.swingx.HorizontalLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class IntlManagerUI {
    private Project project;

    private JPanel mainPanel;
    private JBTable arbTable;
    private DefaultTableModel tableModel;
    private L10nManager manager;
    private boolean isRefresh = false;
    private TableModelEvent modelEvent;
    private TableModelListener tableListener;
    private JTextField filterTextField; // 过滤框

    public IntlManagerUI(Project project) {
        this.project = project;
        manager = L10nManager.getInstance(project);
        tableListener = e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                if (!isRefresh) {
                    if (e.getColumn() == 0) {
                        manager.saveAllLanguages(getAllLocalizations());
                    } else {
                        manager.saveLanguage(tableModel.getColumnName(e.getColumn()), getLocalizations(e.getColumn()));
                    }
                }
            }
        };

        initUI();
        instance = this;
    }


    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());

        initToolBar();
        loadTable();

        mainPanel.add(new JBScrollPane(arbTable), BorderLayout.CENTER);
    }

    private void initToolBar() {
        // 创建工具栏
        JPanel toolbar = new JPanel(new HorizontalLayout());
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        // 刷新
        AnAction refreshAction = new AnAction("刷新", "刷新数据", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                refreshTable();
            }
        };
        actionGroup.add(refreshAction);

        // 新增
        AnAction addAction = new AnAction("添加", "添加一个值", AllIcons.General.Add) {

            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                new AddIntlValueDialog(project).show();

            }
        };
        actionGroup.add(addAction);

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("IntlManagerToolbar", actionGroup, true);

        actionToolbar.setTargetComponent(toolbar);
        toolbar.add(actionToolbar.getComponent());
        // 过滤
        filterTextField = new JTextField(10);
        filterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter(filterTextField.getText());
            }
        });
        toolbar.add(filterTextField);
        mainPanel.add(toolbar, BorderLayout.NORTH);
    }


    private void applyFilter(String filterText) {
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) arbTable.getRowSorter();
        if (filterText == null || filterText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(filterText));
        }
    }


    public void refreshTable() {
        isRefresh = true;
        tableModel.removeTableModelListener(tableListener);
        manager.loadLanguages();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("Key");
        for (int i = 1; i <= manager.getLanguages().size(); i++) {
            tableModel.addColumn(manager.getLanguages().get(i - 1));
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        for (int i = 0; i < arbTable.getColumnCount(); i++) {
            sorter.setSortable(i, true);
        }
        arbTable.setRowSorter(sorter);
        initTableData();
        isRefresh = false;
    }

    private void initTableData() {
        for (String key : manager.getLocalizations().keySet()) {
            String[] rows = new String[manager.getLanguages().size() + 1];
            for (int i = 1; i <= manager.getLanguages().size(); i++) {
                rows[i] = manager.getLocalizations().get(key).get(manager.getLanguages().get(i - 1));
            }
            rows[0] = key;
            tableModel.addRow(rows);
        }
        arbTable.getModel().addTableModelListener(tableListener);
    }

    private void loadTable() {
        String[] columnNames = new String[manager.getLanguages().size() + 1];
        columnNames[0] = "Key";
        for (int i = 1; i <= manager.getLanguages().size(); i++) {
            columnNames[i] = manager.getLanguages().get(i - 1);
        }
        tableModel = new DefaultTableModel(null, columnNames);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);

        arbTable = new JBTable(tableModel);
        // 设置排序
        for (int i = 0; i < arbTable.getColumnCount(); i++) {
            sorter.setSortable(i, true);
        }
        arbTable.setRowSorter(sorter);
        arbTable.getTableHeader().setReorderingAllowed(false);
        // 载入数据
        initTableData();
    }

    private Map<String, Map<String, String>> getAllLocalizations() {
        Map<String, Map<String, String>> localizations = new HashMap<>();
        for (int col = 1; col < tableModel.getColumnCount(); col++) {
            String language = tableModel.getColumnName(col);
            if (!localizations.containsKey(language)) {
                localizations.put(tableModel.getColumnName(col), new HashMap<>());
            }
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                String key = tableModel.getValueAt(row, 0).toString();
                String value = tableModel.getValueAt(row, col).toString();
                localizations.get(language).put(key, value);
            }
        }
        return localizations;
    }

    private Map<String, String> getLocalizations(int col) {
        Map<String, String> localizations = new HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            localizations.put((String) tableModel.getValueAt(i, 0), (String) tableModel.getValueAt(i, col));
        }
        return localizations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JBTable getArbTable() {
        return arbTable;
    }

    public void setArbTable(JBTable arbTable) {
        this.arbTable = arbTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public L10nManager getManager() {
        return manager;
    }

    public void setManager(L10nManager manager) {
        this.manager = manager;
    }

    static private IntlManagerUI instance;

    public static IntlManagerUI getInstance() {
        return instance;
    }
}
