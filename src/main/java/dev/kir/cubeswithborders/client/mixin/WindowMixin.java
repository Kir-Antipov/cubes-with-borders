package dev.kir.cubeswithborders.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.*;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Window.class)
abstract class WindowMixin {
    @Shadow
    private @Final long handle;

    @Shadow
    private int width;

    @Shadow
    private int height;

    @ModifyArg(method = "method_4479", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V", ordinal = 0), index = 1)
    private long enableContextSwitchingOnWindows(long monitor) {
        if (!System.getProperty("os.name").contains("Windows")) {
            return monitor;
        }

        int width = this.width;
        int height = this.height;
        GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        this.width = width;
        this.height = height;

        return 0;
    }

    @Inject(method = "method_4479", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void restoreWindowDecorations(CallbackInfo ci) {
        int width = this.width;
        int height = this.height;
        GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        this.width = width;
        this.height = height;
    }

    @Inject(method = "method_4479", at = @At("RETURN"))
    private void disableAutoIconify(CallbackInfo ci) {
        GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_AUTO_ICONIFY, GLFW.GLFW_FALSE);
    }
}
