package cc.thonly.polydex2rei.mixin;

import dev.architectury.utils.GameInstance;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.impl.common.registry.displays.ServerDisplayRegistryImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.recipebook.ClientRecipeManager;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ServerRecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Mixin(ServerDisplayRegistryImpl.class)
public class ServerDisplayRegistryImplMixin {
    @Shadow
    @Final
    private static Comparator<RecipeEntry<?>> RECIPE_COMPARATOR;

//    @Inject(method = "getAllSortedRecipes", at = @At("HEAD"), cancellable = true)
//    public void getAllSortedRecipes(CallbackInfoReturnable<List<RecipeEntry<?>>> cir) {
//        if (GameInstance.getServer() == null && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
//            MinecraftClient mc = MinecraftClient.getInstance();
//            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
//            if (networkHandler == null) {
//                cir.setReturnValue(new ArrayList<>());
//                cir.cancel();
//                return;
//            }
//            RecipeManager recipeManager = networkHandler.getRecipeManager();
//            if (recipeManager instanceof ClientRecipeManager clientRecipeManager) {
//                SynchronizedRecipes synchronizedRecipes = clientRecipeManager.getSynchronizedRecipes();
//                Collection<RecipeEntry<?>> recipes = synchronizedRecipes.recipes();
//                cir.setReturnValue(recipes.parallelStream().sorted(RECIPE_COMPARATOR).toList());
//                cir.cancel();
//            }
//        }
//    }
}
