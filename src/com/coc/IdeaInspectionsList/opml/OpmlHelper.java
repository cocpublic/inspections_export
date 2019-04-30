package com.coc.IdeaInspectionsList.opml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 生成opml文件
 */
public class OpmlHelper {
    /**
     * 目标根标签
     */
    private static final List<String> targetList = new ArrayList<>(Arrays.asList("Android", "AOP", "Gradle"
            , "Groovy", "Java", "Kotlin", "Kotlin Android", "RegExp", "XML"));


    private Node rootNode = new Node("Inspections");


    /**
     * 多叉树添加节点
     *
     * @param groupPath       上级节点数组
     * @param displayName     终节点名字
     * @param level
     * @param loadDescription
     */
    public void addNode2Tree(String[] groupPath, String displayName, String level, String loadDescription) {
        boolean isTarget = isTarget(groupPath);
        if (!isTarget) return;
        Node currentNode = rootNode;
        for (String path : groupPath) {
            Node sameNode = listContainKey(currentNode.list, path);

            if (sameNode != null) {
                currentNode = sameNode;
            } else {
                Node node = new Node(path);
                currentNode.addNode(node);
                currentNode = node;
            }
        }

        Node subNode = new Node(displayName);
        subNode.addDes(formatdes(loadDescription));
        currentNode.addNode(subNode);
    }


    /**
     * 是否 目标节点
     * @param groupPath
     * @return
     */
    private boolean isTarget(String[] groupPath) {
        if (groupPath == null || groupPath.length == 0) return false;
        String root = groupPath[0];
        return targetList.contains(root);
    }


    /**
     * 移除 description html中冗余的标签
     *
     * @param description
     * @return
     */
    private String formatdes(String description) {
        if (description == null || description.length() == 0) return null;
        return description
                .replaceAll("<html>", "")
                .replaceAll("</html>", "")
                .replaceAll("<body>", "")
                .replaceAll("</body>", "")
                .replaceAll("<br>", "\n")
                .replaceAll("<p>", "\n")
                .replaceAll("<code>", " **")
                .replaceAll("</code>", "** ")
                .replaceAll("<b>", " **")
                .replaceAll("</b>", "** ")
                .replaceAll("\"", "\'")
                .replaceAll("\n\n", "\n")
                ;
    }




    /**
     * 查看是否已存在相同父节点
     *
     * @param list
     * @param key
     * @return
     */
    private Node listContainKey(List<Node> list, String key) {
        if (list == null) return null;

        for (Node node : list) {
            if (node._title.equalsIgnoreCase(key)) return node;
        }
        return null;
    }


    /**
     * 保存多叉树为opml文件
     * @param dir
     * @param filename
     * @return
     */
    public boolean saveOpml(String dir, String filename) {
        File file = new File(dir, filename);
        String opmlString = printTree2Opml();
        boolean result = false;

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(opmlString);

            writer.flush();//刷新内存，将内存中的数据立刻写出。
            writer.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private StringBuilder stringBuilder;

    /**
     * 生成OPML文件
     * @return
     */
    private String printTree2Opml() {
        stringBuilder = new StringBuilder();

        //添加opml头
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<opml version=\"2.0\"><head><title>Inspection列表</title></head><body>");

        //录入正文
        printTree(rootNode);

        //录入尾巴
        stringBuilder.append("\n</body></opml>");
        return stringBuilder.toString();
    }


    /**
     * 按格式打印多叉树
     * @param node
     */
    private void printTree(Node node) {
        if (node == null) return;

        stringBuilder.append("<outline text=\"");
        stringBuilder.append(node._title);
        if (node._note != null) {
            stringBuilder.append("\" _note=\"");
            stringBuilder.append(node._note);
        }
        stringBuilder.append("\" >");


        if (node.list != null) {
            for (Node subNode : node.list) {
                printTree(subNode);
            }
        }

        stringBuilder.append("</outline>");
    }



    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        OpmlHelper utils = new OpmlHelper();
        utils.addNode2Tree(new String[]{"Java", "Abstraction issues"}, "Cast to a concrete class", "w", "des");
        utils.addNode2Tree(new String[]{"Java", "Abstraction issues"}, "Chain of'instanceOf", "w", "des");
        utils.addNode2Tree(new String[]{"Java", "Abstraction issues"}, "Feature envy", "w", "des");
        utils.addNode2Tree(new String[]{"Java", "Imports"}, "Import from same package", "w", "des");
        utils.addNode2Tree(new String[]{"Android", "Lint", "Performance", "Application Size"}, "Duplicate Strings", "w", "des");
        utils.addNode2Tree(new String[]{"Android", "Lint", "Performance", "Application Size"}, "Singe Strings", "w", "des");
        utils.addNode2Tree(new String[]{"Android", "Lint", "Performance"}, "Synthetic Accessor", "w", "des");
        utils.addNode2Tree(new String[]{"Android", "Lint", "Performance"}, "Unused id", "w", "des");
        utils.addNode2Tree(new String[]{"CSS", "Lint", "Performance"}, "Unused id", "w", "<html><body>Combining Ellipsize and Maxlines<br><br>Combining <code>ellipsize</code> and <code>maxLines=1</code> can lead to crashes on some devices. Earlier versions of lint recommended replacing <code>singleLine=true</code> with <code>maxLines=1</code> but that should not be done when using <code>ellipsize</code>.<br><br>Issue id: EllipsizeMaxLines<br><br><a href=\"https://issuetracker.google.com/issues/36950033\">https://issuetracker.google.com/issues/36950033</a></body></html>");
        utils.addNode2Tree(new String[]{"Android", "Lint", "Performance"}, "Unused", "w", "<html><body>Combining Ellipsize and Maxlines<br><br>Combining <code>ellipsize</code> and <code>maxLines=1</code> can lead to crashes on some devices. Earlier versions of lint recommended replacing <code>singleLine=true</code> with <code>maxLines=1</code> but that should not be done when using <code>ellipsize</code>.<br><br>Issue id: EllipsizeMaxLines<br><br><a href=\"https://issuetracker.google.com/issues/36950033\">https://issuetracker.google.com/issues/36950033</a></body></html>");
        System.out.println(utils.printTree2Opml());
    }

}
