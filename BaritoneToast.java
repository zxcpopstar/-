package dev.luvbeeq.baritone.api.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.mojang.blaze3d.matrix.MatrixStack;
import net.mojang.blaze3d.platform.GlStateManager;

public class BaritoneToast implements IToast {
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;
    private final long totalShowTime;

    public BaritoneToast(ITextComponent titleComponent, ITextComponent subtitleComponent, long totalShowTime) {
        this.title = titleComponent.getString();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getString();
        this.totalShowTime = totalShowTime;
    }

    public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }

        toastGui.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/toasts.png"));
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 255.0f);
        toastGui.blit(matrixStack, 0, 0, 0, 32, 160, 32);

        if (this.subtitle == null) {
            toastGui.getMinecraft().fontRenderer.drawString(matrixStack, this.title, 18, 12, -11534256);
        } else {
            toastGui.getMinecraft().fontRenderer.drawString(matrixStack, this.title, 18, 7, -11534256);
            toastGui.getMinecraft().fontRenderer.drawString(matrixStack, this.subtitle, 18, 18, -16777216);
        }

        return delta - this.firstDrawTime < totalShowTime ? Visibility.SHOW : Visibility.HIDE;
    }

    public void setDisplayedText(ITextComponent titleComponent, ITextComponent subtitleComponent) {
        this.title = titleComponent.getString();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getString();
        this.newDisplay = true;
    }

    public static void addOrUpdate(ToastGui toast, ITextComponent title, ITextComponent subtitle, long totalShowTime) {
        BaritoneToast baritonetoast = toast.getToast(BaritoneToast.class, new Object());

        if (baritonetoast == null) {
            toast.add(new BaritoneToast(title, subtitle, totalShowTime));
        } else {
            baritonetoast.setDisplayedText(title, subtitle);
        }
    }

    public static void addOrUpdate(ITextComponent title, ITextComponent subtitle) {
        addOrUpdate(Minecraft.getInstance().getToastGui(), title, subtitle, dev.luvbeeq.baritone.api.BaritoneAPI.getSettings().toastTimer.value);
    }
}
