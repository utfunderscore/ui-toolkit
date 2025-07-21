package org.readutf.ui.items;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.readutf.ui.Constants;
import org.readutf.ui.UIToolkit;
import org.readutf.ui.utils.TextureUtils;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.ReferenceItemModel;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.model.ModelTextures;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;
import team.unnamed.creative.texture.Texture;

import java.util.Collections;
import java.util.List;

public class ItemOverride implements ResourcePackPart {

    private @NotNull final Texture texture;
    private @NotNull final Key key;

    public ItemOverride(@NotNull Writable writable, @NotNull Metadata metadata) {
        this.texture = Constants.createItemTexture(writable, metadata);
        int modelId = Constants.getNextModelId();
        this.key = Key.key(UIToolkit.namespace, "%s".formatted(modelId));
    }

    @Override
    public void addTo(@NotNull ResourceContainer container) {

        ModelTexture modelTexture = ModelTexture.ofKey(TextureUtils.removeExtension(texture.key()));
        Model model = Model.model()
                .parent(Model.ITEM_GENERATED)
                .key(key)
                .textures(ModelTextures.of(List.of(modelTexture), null, Collections.emptyMap()))
                .build();

        ReferenceItemModel itemModel = ItemModel.reference(model.key());

        Item item = Item.item(key, itemModel);

        container.texture(texture);
        container.model(model);
        container.item(item);
    }

    public String getItemKey() {
        return key.asString();
    }

    public static @NotNull ItemOverride create(@NotNull Writable writable) {
        return new ItemOverride(writable, Metadata.empty());
    }

    public static @NotNull ItemOverride create(@NotNull Writable writable, @NotNull Metadata metadata) {
        return new ItemOverride(writable, metadata);
    }
}
