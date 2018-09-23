package net.bdew.wurm.server.tips;

import javassist.ClassPool;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerTipsMod implements WurmServerMod, PreInitable, Initable, ServerStartedListener {
    private static final Logger logger = Logger.getLogger("ServerTips");

    public static int minPower = 0;

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    public static void logWarning(String msg) {
        if (logger != null)
            logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }


    @Override
    public void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();

            classPool.getCtClass("com.wurmonline.server.items.Item").getMethod("getHoverText", "()Ljava/lang/String;")
                    .insertAfter("return net.bdew.wurm.server.tips.Hooks.getItemTip(this, $_);");

            classPool.getCtClass("com.wurmonline.server.creatures.Creature").getMethod("getHoverText", "()Ljava/lang/String;")
                    .insertAfter("return net.bdew.wurm.server.tips.Hooks.getCreatureTip(this, $_);");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void onServerStarted() {
        try {
            EnchantUtils.prepare();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
