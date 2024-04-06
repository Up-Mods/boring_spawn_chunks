package io.github.ennuil.boring_spawn_chunks.modmenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ButtonWidget;
import net.minecraft.client.gui.widget.layout.FrameWidget;
import net.minecraft.client.gui.widget.layout.GridWidget;
import net.minecraft.client.gui.widget.text.MultilineTextWidget;
import net.minecraft.client.gui.widget.text.TextWidget;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class RedirectionScreen extends Screen {
	private static final Text TITLE = Text.translatable("boring_spawn_chunks.modmenu_screen.title");
	private static final Text MESSAGE = Text.translatable("boring_spawn_chunks.modmenu_screen.message");

	private final Screen parent;
	private final GridWidget grid = new GridWidget().setColumnSpacing(10).setRowSpacing(20);

	protected RedirectionScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		var additionHelper = this.grid.createAdditionHelper(2);
		var layoutSettings = additionHelper.copyDefaultSettings().alignHorizontallyCenter();
		additionHelper.add(new TextWidget(this.title, this.textRenderer), 2, layoutSettings);
		var multilineTextWidget = additionHelper.add(new MultilineTextWidget(MESSAGE, this.textRenderer).setCentered(true), 2, layoutSettings);
		multilineTextWidget.setMaxWidth(310);

		if (QuiltLoader.isModLoaded("boring_default_game_rules")) {
			additionHelper.add(ButtonWidget.builder(CommonTexts.PROCEED, button -> this.closeScreen()).build(), 2, layoutSettings);
		} else {
			additionHelper.add(ButtonWidget.builder(CommonTexts.OPEN_LINK, button -> Util.getOperatingSystem().open("https://modrinth.com/mod/boring-default-game-rules")).build());
			additionHelper.add(ButtonWidget.builder(CommonTexts.BACK, button -> this.closeScreen()).build());
		}

		this.grid.arrangeElements();
		this.grid.visitWidgets(this::addDrawableSelectableElement);
		this.repositionElements();
	}

	@Override
	protected void repositionElements() {
		FrameWidget.align(this.grid, this.getArea());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics, mouseX, mouseY, delta);
		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void closeScreen() {
		this.client.setScreen(parent);
	}
}
