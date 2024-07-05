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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Window.class)
abstract class WindowMixin {
    @Shadow
    private @Final long handle;

    @Inject(method = "method_4479", at = @At("RETURN"))
    private void disableAutoIconify(CallbackInfo ci) {
        GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_AUTO_ICONIFY, GLFW.GLFW_FALSE);
    }
}
