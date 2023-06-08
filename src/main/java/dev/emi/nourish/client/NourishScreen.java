package dev.emi.nourish.client;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.nourish.NourishComponent;
import dev.emi.nourish.NourishHolder;
import dev.emi.nourish.groups.NourishGroup;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NourishScreen extends Screen {
	private static final Identifier GUI_TEX = new Identifier("nourish", "textures/gui/gui.png");
	private boolean returnToInv;
	private int maxNameLength = 0;
	private int w;
	private int h;
	private int x;
	private int y;	

	public NourishScreen() {
		super(Text.translatable("nourish.gui.nourishment"));
	}

	public NourishScreen(boolean returnToInv) {
		this();
		this.returnToInv = returnToInv;
	}

	@Override
	public void init() {
		super.init();
		List<NourishGroup> groups = NourishHolder.NOURISH.get(client.player).getProfile().groups;
		for (NourishGroup group: groups) {
			int l = this.textRenderer.getWidth(Text.translatable("nourish.group." + group.identifier.getPath()).getString());
			if (l > maxNameLength) {
				maxNameLength = l;
			}
		}
		w = maxNameLength + 120;
		h = 34 + groups.size() * 20;
		if (groups.size() > 0 && groups.get(groups.size() - 1).secondary) {
			h += 24;
		}
		x = (width - w) / 2 - 2;
		y = (height - h) / 2 - 2;
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		this.renderBackground(ctx);
		ctx.drawTexture(GUI_TEX, x + 4, y + 4, 4 * w, 3 * h, w - 4, h - 4, 256 * w, 256 * h);
		ctx.drawTexture(GUI_TEX, x + 4, y, 4 * w, 0, w - 4, 4, 256 * w, 256);
		ctx.drawTexture(GUI_TEX, x + 4, y + h, 3 * w, 4, w - 4, 4, 256 * w, 256);
		ctx.drawTexture(GUI_TEX, x, y + 4, 0, 4 * h, 4, h - 4, 256, 256 * h);
		ctx.drawTexture(GUI_TEX, x + w, y + 4, 4, 3 * h, 4, h - 4, 256, 256 * h);
		ctx.drawTexture(GUI_TEX, x, y, 0, 0, 4, 4);
		ctx.drawTexture(GUI_TEX, x + w, y, 4, 0, 4, 4);
		ctx.drawTexture(GUI_TEX, x, y + h, 0, 4, 4, 4);
		ctx.drawTexture(GUI_TEX, x + w, y + h, 4, 4, 4, 4);
		int yo = 28;
		boolean secondary = false;
		for (NourishGroup group: NourishHolder.NOURISH.get(client.player).getProfile().groups) {
			if (group.secondary && !secondary) {
				secondary = true;
				Text t = Text.translatable("nourish.gui.secondary");
				int sw = this.textRenderer.getWidth(t.getString());
				ctx.drawText(textRenderer, t, x + w / 2 - sw / 2, y + yo + 4, 4210752, false);
				yo += 20;
			}
			int color = group.getColor() | 0xFF000000;
			ctx.drawText(this.textRenderer, Text.translatable("nourish.group." + group.identifier.getPath()), x + 10, y + yo + 4, 4210752, false);
			NourishComponent comp = NourishHolder.NOURISH.get(client.player);
			ctx.drawTexture(GUI_TEX, x + maxNameLength + 20, y + yo + 2, 0, 8, 90, 12);
			ctx.fill(x + maxNameLength + 21, y + yo + 3, x + maxNameLength + 21 + Math.round(88 * comp.getValue(group)), y + yo + 13, color);
			if (mouseX > x + maxNameLength + 20 && mouseY > y + yo + 2 && mouseX < x + maxNameLength + 108 && mouseY < y + yo + 13) {
				if (group.description) {
					List<OrderedText> lines = Lists.newArrayList();
					lines.add(Text.translatable("nourish.group.description." + group.identifier.getPath()).asOrderedText());
					this.setTooltip(lines);
				}
			}
			yo += 20;
		}
		int tw = this.textRenderer.getWidth(this.title.getString());
		ctx.drawText(this.textRenderer, this.title.getString(), (width - tw) / 2, y + 6, 4210752, false);
		super.render(ctx, mouseX, mouseY, delta);
	}

	public boolean keyPressed(int int_1, int int_2, int int_3) {
		if (client.options.inventoryKey.matchesKey(int_1, int_2)) {
			if (returnToInv) {
				client.setScreen(new InventoryScreen(client.player));
			} else {
				this.close();
			}
			return true;
		} else {
			return super.keyPressed(int_1, int_2, int_3);
		}
	}

	public void close() {
		if (returnToInv) {
			client.setScreen(new InventoryScreen(client.player));
			return;
		}
		super.close();
	}

	public boolean shouldPause() {
		return false;
	}
}