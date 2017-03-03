package android.com.solutions.nerd.cruising.ui.views;

import android.com.solutions.nerd.cruising.models.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cberman on 3/2/2017.
 */

public class AvatarView extends View {
    public static final String IMAGE_URI_ROOT = "https://habitica-assets.s3.amazonaws.com/mobileApp/images/";
    private static final String TAG = "AvatarView";
    private static final Map<String, String> FILENAME_MAP;
    private static final Rect FULL_HERO_RECT = new Rect(0, 0, 140, 147);
    private static final Rect COMPACT_HERO_RECT = new Rect(0, 0, 114, 114);
    private static final Rect HERO_ONLY_RECT = new Rect(0, 0, 90, 90);
    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("head_special_1", "ContributorOnly-Equip-CrystalHelmet.gif");
        tempMap.put("armor_special_1", "ContributorOnly-Equip-CrystalArmor.gif");
        tempMap.put("weapon_special_critical", "weapon_special_critical.gif");
        tempMap.put("Pet-Wolf-Cerberus", "Pet-Wolf-Cerberus.gif");
        FILENAME_MAP = Collections.unmodifiableMap(tempMap);
    }


    private boolean showBackground = true;
    private boolean showMount = true;
    private boolean showPet = true;
    private boolean hasBackground;
    private boolean hasMount;
    private boolean hasPet;
    private boolean isOrphan;
    // private MultiDraweeHolder<GenericDraweeHierarchy> multiDraweeHolder = new MultiDraweeHolder<>();
    private User user;

    private RectF avatarRectF;
    private Matrix matrix = new Matrix();
    private AtomicInteger numberLayersInProcess = new AtomicInteger(0);
    private Consumer<Bitmap> avatarImageConsumer;
    private Bitmap avatarBitmap;
    private Canvas avatarCanvas;
    private Map<LayerType, String> currentLayers;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */

    public AvatarView(Context context) {
        super(context);
        init(null, 0);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public AvatarView(Context context, boolean showBackground, boolean showMount, boolean showPet) {
        super(context);

        this.showBackground = showBackground;
        this.showMount = showMount;
        this.showPet = showPet;
        isOrphan = true;
    }
    public void configureView(boolean showBackground, boolean showMount, boolean showPet) {
        this.showBackground = showBackground;
        this.showMount = showMount;
        this.showPet = showPet;
    }

    private void init(AttributeSet attrs, int defStyle) {
//        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.AvatarView, defStyle, 0);
//
//        try {
//            showBackground = a.getBoolean(R.styleable.AvatarView_showBackground, true);
//            showMount = a.getBoolean(R.styleable.AvatarView_showMount, true);
//            showPet = a.getBoolean(R.styleable.AvatarView_showPet, true);
//        } finally {
//            a.recycle();
//        }
    }
    private void showLayers(@NonNull Map<LayerType, String> layerMap) {
//        if (multiDraweeHolder.size() > 0) return;
        int i = 0;

        currentLayers = layerMap;

        numberLayersInProcess.set(layerMap.size());

        for (Map.Entry<LayerType, String> entry : layerMap.entrySet()) {
            final LayerType layerKey = entry.getKey();
            final String layerName = entry.getValue();
            final int layerNumber = i++;
//
//            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
//                    .setFadeDuration(0)
//                    .build();
//
//            DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, getContext());
//            draweeHolder.getTopLevelDrawable().setCallback(this);
//            multiDraweeHolder.add(draweeHolder);
//
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(IMAGE_URI_ROOT + getFileName(layerName))
//                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
//                        @Override
//                        public void onFinalImageSet(
//                                String id,
//                                ImageInfo imageInfo,
//                                Animatable anim) {
//                            if (imageInfo != null) {
//                                if (multiDraweeHolder.size() > layerNumber) {
//                                    multiDraweeHolder.get(layerNumber).getTopLevelDrawable().setBounds(getLayerBounds(layerKey, layerName, imageInfo));
//                                }
//                                onLayerComplete();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String id, Throwable throwable) {
//                            Log.e(TAG, "Error loading layer: " + layerName, throwable);
//                            onLayerComplete();
//                        }
//                    })
//                    .setAutoPlayAnimations(!isOrphan)
//                    .build();
//            draweeHolder.setController(controller);
        }

        //   if (isOrphan) multiDraweeHolder.onAttach();
    }

    private String getFileName(@NonNull String imageName) {
        if (FILENAME_MAP.containsKey(imageName)) {
            return FILENAME_MAP.get(imageName);
        }

        return imageName + ".png";
    }

    private void onLayerComplete() {
        if (numberLayersInProcess.decrementAndGet() == 0) {
            if (avatarImageConsumer != null) {
                avatarImageConsumer.accept(getAvatarImage());
            }
        }
    }

    private Rect getOriginalRect() {
        return (showMount || showPet) ? FULL_HERO_RECT : ((showBackground) ? COMPACT_HERO_RECT : HERO_ONLY_RECT);
    }

    private Bitmap getAvatarImage() {
        assert user != null;
        assert avatarRectF != null;
        Rect canvasRect = new Rect();
        avatarRectF.round(canvasRect);
        avatarBitmap = Bitmap.createBitmap(canvasRect.width(), canvasRect.height(), Bitmap.Config.ARGB_8888);
        avatarCanvas = new Canvas(avatarBitmap);
        draw(avatarCanvas);

        return avatarBitmap;
    }

    private void initAvatarRectMatrix() {
        if (avatarRectF == null) {
            Rect srcRect = getOriginalRect();

            if (isOrphan) {
                avatarRectF = new RectF(srcRect);

                // change scale to not be 1:1
                // a quick fix as fresco AnimatedDrawable/ScaleTypeDrawable
                // will not translate matrix properly
                matrix.setScale(1.2f, 1.2f);
                matrix.mapRect(avatarRectF);
            } else {
                // full hero box when showMount and showPet is enabled (140w * 147h)
                // compact hero box when only showBackground is enabled (114w * 114h)
                // hero only box when all show settings disabled (90w * 90h)
                avatarRectF = new RectF(0, 0, getWidth(), getHeight());
                matrix.setRectToRect(new RectF(srcRect), avatarRectF, Matrix.ScaleToFit.START); // TODO support other ScaleToFit
                avatarRectF = new RectF(srcRect);
                matrix.mapRect(avatarRectF);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initAvatarRectMatrix();
//
//        // draw only when user is set
//        if (user == null) return;
//
//        // request image layers if not yet processed
//        if (multiDraweeHolder.size() == 0) {
//            showLayers(getLayerMap());
//        }
//
//        // manually call onAttach/onDetach if view is without parent as they will never be called otherwise
//        if (isOrphan) multiDraweeHolder.onAttach();
//        multiDraweeHolder.draw(canvas);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        invalidate();
        if (avatarCanvas != null) draw(avatarCanvas);
    }

    public enum LayerType {
        BACKGROUND(0),
        MOUNT_BODY(1),
        CHAIR(2),
        BACK(3),
        SKIN(4),
        SHIRT(5),
        ARMOR(6),
        BODY(7),
        HEAD_0(8),
        HAIR_BASE(9),
        HAIR_BANGS(10),
        HAIR_MUSTACHE(11),
        HAIR_BEARD(12),
        EYEWEAR(13),
        VISUAL_BUFF(14),
        HEAD(15),
        HEAD_ACCESSORY(16),
        HAIR_FLOWER(17),
        SHIELD(18),
        WEAPON(19),
        MOUNT_HEAD(20),
        ZZZ(21),
        PET(22);

        final int order;

        LayerType(int order) {
            this.order = order;
        }
    }

    public interface Consumer<T> {
        void accept(T t);
    }
}