package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions.MARGIN;
import static com.shatteredpixel.shatteredpixeldungeon.windows.WndSupportPrompt.WIDTH_L;
import static com.shatteredpixel.shatteredpixeldungeon.windows.WndSupportPrompt.WIDTH_P;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Image;

public class WndPicture extends WndTabbed {

    public WndPicture(String assets, float scale) {
        super();

        Image image = new Image(assets);
        image.scale.set(scale);
        IconTitle titlebar = new IconTitle(image, null);
        titlebar.setRect( 0, 0, width, 0 );
        add( titlebar );

        resize( (int)image.width(), (int)image.height()  );

    }
    public WndPicture(String assets, float scale, String text) {
        super();


        // 创建并加载图片
        Image image = new Image(assets);
        image.scale.set(scale);

        // 创建并设置图像
        IconTitle icon = new IconTitle(image, null);
        icon.setRect(0, 0, image.width(), image.height());
        add(icon);

        // 创建并添加文本
        RenderedTextBlock info = PixelScene.renderTextBlock(text, 6);
        info.maxWidth((int) ((PixelScene.landscape() ? WIDTH_L : WIDTH_P) - image.width() - MARGIN * 3)); // 图片宽度 + 间距
        info.setPos(image.width() + MARGIN * 2, MARGIN);
        add(info);

        // 调整对话框大小以适应图片和文本
        int totalWidth = (int) (image.width() + info.width() + MARGIN * 3); // 图片宽度 + 文本宽度 + 间距
        int totalHeight = Math.max((int) image.height(), (int) info.height()) + MARGIN * 2;
        resize(totalWidth, totalHeight);

    }


    public WndPicture(String assets, float scalex, float scaley) {
        super();

        Image image = new Image(assets);
        image.scale.set(scalex, scaley);
        IconTitle titlebar = new IconTitle(image, null);
        titlebar.setRect( 0, 0, width, 0 );
        add( titlebar );

        resize( (int)image.width(), (int)image.height()  );
    }
}