package com.ephod.morpheus.databinding;
import com.ephod.morpheus.R;
import com.ephod.morpheus.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityNowPlayingBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.visualizerView, 1);
        sViewsWithIds.put(R.id.artistname, 2);
        sViewsWithIds.put(R.id.albumart, 3);
        sViewsWithIds.put(R.id.songdetails, 4);
        sViewsWithIds.put(R.id.songname, 5);
        sViewsWithIds.put(R.id.albumname, 6);
        sViewsWithIds.put(R.id.controls, 7);
        sViewsWithIds.put(R.id.previousbutton, 8);
        sViewsWithIds.put(R.id.holoCircularProgressBar, 9);
        sViewsWithIds.put(R.id.playbutton, 10);
        sViewsWithIds.put(R.id.forwardbutton, 11);
        sViewsWithIds.put(R.id.shufflebutton, 12);
        sViewsWithIds.put(R.id.repeatbutton, 13);
        sViewsWithIds.put(R.id.searchbutton, 14);
        sViewsWithIds.put(R.id.morebutton, 15);
    }
    // views
    @NonNull
    public final com.makeramen.roundedimageview.RoundedImageView albumart;
    @NonNull
    public final android.widget.TextView albumname;
    @NonNull
    public final android.widget.TextView artistname;
    @NonNull
    public final android.widget.LinearLayout controls;
    @NonNull
    public final android.widget.ImageButton forwardbutton;
    @NonNull
    public final com.ephod.morpheus.CircularSeekBar holoCircularProgressBar;
    @NonNull
    public final android.widget.ImageButton morebutton;
    @NonNull
    public final android.widget.RelativeLayout parentholder;
    @NonNull
    public final android.widget.ImageButton playbutton;
    @NonNull
    public final android.widget.ImageButton previousbutton;
    @NonNull
    public final android.widget.ImageButton repeatbutton;
    @NonNull
    public final android.widget.ImageButton searchbutton;
    @NonNull
    public final android.widget.ImageButton shufflebutton;
    @NonNull
    public final android.widget.LinearLayout songdetails;
    @NonNull
    public final android.widget.TextView songname;
    @NonNull
    public final com.ephod.morpheus.visualizer.VisualizerView visualizerView;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityNowPlayingBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 16, sIncludes, sViewsWithIds);
        this.albumart = (com.makeramen.roundedimageview.RoundedImageView) bindings[3];
        this.albumname = (android.widget.TextView) bindings[6];
        this.artistname = (android.widget.TextView) bindings[2];
        this.controls = (android.widget.LinearLayout) bindings[7];
        this.forwardbutton = (android.widget.ImageButton) bindings[11];
        this.holoCircularProgressBar = (com.ephod.morpheus.CircularSeekBar) bindings[9];
        this.morebutton = (android.widget.ImageButton) bindings[15];
        this.parentholder = (android.widget.RelativeLayout) bindings[0];
        this.parentholder.setTag(null);
        this.playbutton = (android.widget.ImageButton) bindings[10];
        this.previousbutton = (android.widget.ImageButton) bindings[8];
        this.repeatbutton = (android.widget.ImageButton) bindings[13];
        this.searchbutton = (android.widget.ImageButton) bindings[14];
        this.shufflebutton = (android.widget.ImageButton) bindings[12];
        this.songdetails = (android.widget.LinearLayout) bindings[4];
        this.songname = (android.widget.TextView) bindings[5];
        this.visualizerView = (com.ephod.morpheus.visualizer.VisualizerView) bindings[1];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static ActivityNowPlayingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityNowPlayingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<ActivityNowPlayingBinding>inflate(inflater, com.ephod.morpheus.R.layout.activity_now_playing, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static ActivityNowPlayingBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityNowPlayingBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.ephod.morpheus.R.layout.activity_now_playing, null, false), bindingComponent);
    }
    @NonNull
    public static ActivityNowPlayingBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static ActivityNowPlayingBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/activity_now_playing_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new ActivityNowPlayingBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}