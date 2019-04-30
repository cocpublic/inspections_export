package com.coc.IdeaInspectionsList.opml;

import com.intellij.util.containers.SortedList;

import java.util.Comparator;
import java.util.List;

/**
 * 多叉树
 */
public class Node {
    public String _title;
    public String _note;
    public List<Node> list;

    public Node(String title) {
        this._title = title;
    }

    public void addNode(Node node) {
        if (list == null) {
            list = new SortedList<>(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1._title.compareToIgnoreCase(o2._title);
                }
            });
        }
        list.add(node);
    }


    public void addDes(String description) {
        if (description == null || description.length() == 0) return;
        this._note = description;
    }
}
