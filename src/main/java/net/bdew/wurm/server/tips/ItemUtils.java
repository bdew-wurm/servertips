package net.bdew.wurm.server.tips;

import com.wurmonline.server.behaviours.MethodsItems;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;

import java.util.Set;

public class ItemUtils {
    public static String getImproveString(Item item) {
        int templateId = -10;
        if (item.isRepairable() && item.creationState == 0) {
            if (!item.isNewbieItem() && !item.isChallengeNewbieItem()) {
                templateId = MethodsItems.getImproveTemplateId(item);
            }
        } else if (item.creationState != 0) {
            templateId = MethodsItems.getItemForImprovement(MethodsItems.getImproveMaterial(item), item.creationState);
        }
        if (item.getTemplateId() == 1307 && item.getData1() <= 0) {
            if (item.getAuxData() >= 65) {
                templateId = 441;
            } else {
                templateId = 97;
            }
        }
        if (templateId != -10L) {
            try {
                return ItemTemplateFactory.getInstance().getTemplate(templateId).getName();
            } catch (NoSuchTemplateException nst) {
                return null;
            }
        }
        return null;
    }

    public static void refreshItem(Item item) {
        if (item.getParentId() == -10L && item.getZoneId() > 0) {
            VolaTile vt = Zones.getTileOrNull(item.getTilePos(), item.isOnSurface());
            vt.makeInvisible(item);
            vt.makeVisible(item);
        }

        Set<Creature> watchers = item.getWatcherSet();
        if (watchers != null) {

            long inventoryWindow = item.getTopParent();

            if (item.isInside(1333, 1334)) {
                inventoryWindow = item.getFirstParent(1333, 1334).getWurmId();
            }

            final Item parentWindow = item.recursiveParentCheck();
            if (parentWindow != null && parentWindow != item) {
                inventoryWindow = parentWindow.getWurmId();
            }

            for (Creature watcher : watchers) {
                if (watcher.isPlayer() && watcher.hasLink()) {
                    if (watcher.getInventory().getWurmId() == inventoryWindow) {
                        watcher.getCommunicator().sendRemoveFromInventory(item, -1L);
                        watcher.getCommunicator().sendAddToInventory(item, -1L, -1L, -1);
                    } else {
                        watcher.getCommunicator().sendRemoveFromInventory(item, inventoryWindow);
                        watcher.getCommunicator().sendAddToInventory(item, inventoryWindow, inventoryWindow, -1);
                    }
                }
            }
        }
    }
}
