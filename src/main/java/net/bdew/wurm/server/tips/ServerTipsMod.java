package net.bdew.wurm.server.tips;

import javassist.ClassPool;
import javassist.CtClass;
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

            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            CtClass ctDbItem = classPool.getCtClass("com.wurmonline.server.items.DbItem");
            CtClass ctTempItem = classPool.getCtClass("com.wurmonline.server.items.TempItem");
            CtClass ctCreature = classPool.getCtClass("com.wurmonline.server.creatures.Creature");
            CtClass ctSpellEffect = classPool.getCtClass("com.wurmonline.server.spells.SpellEffect");
            CtClass ctItemSpellEffects = classPool.getCtClass("com.wurmonline.server.items.ItemSpellEffects");

            // Hook hover text methofs

            ctItem.getMethod("getHoverText", "()Ljava/lang/String;")
                    .insertAfter("return net.bdew.wurm.server.tips.Hooks.getItemTip(this, $_);");

            ctCreature.getMethod("getHoverText", "()Ljava/lang/String;")
                    .insertAfter("return net.bdew.wurm.server.tips.Hooks.getCreatureTip(this, $_);");

            // Update hover when enchants are added/changed/removed

            ctItemSpellEffects.getMethod("addSpellEffect", "(Lcom/wurmonline/server/spells/SpellEffect;)V")
                    .insertAfter("net.bdew.wurm.server.tips.Hooks.spellEffectChanged($1);");

            ctSpellEffect.getMethod("improvePower", "(F)V")
                    .insertAfter("net.bdew.wurm.server.tips.Hooks.spellEffectChanged(this);");

            ctSpellEffect.getMethod("delete", "()V")
                    .insertAfter("net.bdew.wurm.server.tips.Hooks.spellEffectChanged(this);");

            // Update on damage and quality change

            ctDbItem.getMethod("setDamage", "(F)Z").insertAfter("net.bdew.wurm.server.tips.Hooks.itemChanged(this);");
            ctDbItem.getMethod("setQualityLevel", "(F)Z").insertAfter("net.bdew.wurm.server.tips.Hooks.itemChanged(this);");
            ctTempItem.getMethod("setDamage", "(F)Z").insertAfter("net.bdew.wurm.server.tips.Hooks.itemChanged(this);");
            ctTempItem.getMethod("setQualityLevel", "(F)Z").insertAfter("net.bdew.wurm.server.tips.Hooks.itemChanged(this);");


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
