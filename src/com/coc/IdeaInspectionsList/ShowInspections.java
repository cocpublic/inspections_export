package com.coc.IdeaInspectionsList;

import com.coc.IdeaInspectionsList.opml.OpmlHelper;
import com.intellij.codeInspection.ex.InspectionProfileImpl;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.profile.codeInspection.InspectionProfileManager;
import org.jetbrains.annotations.SystemIndependent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class ShowInspections extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        String providersList = listProfile(anActionEvent);
        StringSelection stringSelection = new StringSelection(providersList);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Messages.showMessageDialog("All available inspections:\n" + providersList, "Information", Messages.getInformationIcon());
    }

    private String listProfile(AnActionEvent anActionEvent) {
        @SystemIndependent String projectFilePath2 = anActionEvent.getProject().getProjectFile().getParent().getParent().getPath();
        OpmlHelper opmlHelper = new OpmlHelper();
        String providersList = "listProfile_";

        Collection<InspectionProfileImpl> profiles = InspectionProfileManager.getInstance().getProfiles();
        providersList += "profiles_size= " + profiles.size() + "\n";
        int size = 0;

        for (InspectionProfileImpl profile : profiles) {
            InspectionToolWrapper[] inspectionTools = profile.getInspectionTools(null);
            providersList += "inspectionTools_length = " + inspectionTools.length + "\n";
            for (InspectionToolWrapper toolWrapper : inspectionTools) {
                //providersList += "getGroupPath ="+Arrays.toString(toolWrapper.getGroupPath())+"\n\n";

                opmlHelper.addNode2Tree(toolWrapper.getGroupPath(), toolWrapper.getDisplayName(), toolWrapper.getDefaultLevel().toString(), toolWrapper.loadDescription());
                //opmlHelper.addNode2Tree(toolWrapper.getGroupPath(),toolWrapper.getDisplayName(),toolWrapper.getDefaultLevel().toString(),"des");
                //if (size >= 100) break;
                size++;
            }

        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");//设置日期格式
        boolean saveOpml = opmlHelper.saveOpml(projectFilePath2, "inslist_"+df.format(new Date())+".opml");
        return providersList+ "\n导出结果：" + saveOpml+ "\n保存目录；"+projectFilePath2 ;
    }

}
