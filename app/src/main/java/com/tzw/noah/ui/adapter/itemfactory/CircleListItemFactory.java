package com.tzw.noah.ui.adapter.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tzw.noah.R;
import com.tzw.noah.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.sketchsample.adapter.BindAssemblyRecyclerItem;
import me.xiaopan.sketchsample.widget.SampleImageView;
import me.xiaopan.sketchsample.widget.SampleImageViewHead;

public class CircleListItemFactory extends AssemblyRecyclerItemFactory<CircleListItemFactory.CircleItem> {

    private OnImageClickListener onImageClickListener;
    private int itemSize;

    public CircleListItemFactory(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public boolean isTarget(Object o) {
        return o instanceof String;
    }

    @Override
    public CircleItem createAssemblyItem(ViewGroup viewGroup) {

        return new CircleItem(R.layout.circle_item, viewGroup);
    }

    public interface OnImageClickListener {
        void onClickImage(int position, String optionsKey);
    }

    public class CircleItem extends BindAssemblyRecyclerItem<String> {
        @BindView(R.id.iv_head)
        SampleImageViewHead imageView;
        @BindView(R.id.ll_user)
        RelativeLayout ll_user;
        @BindView(R.id.ll_picture)
        LinearLayout ll_picture;

        Context mContext;

        public CircleItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onConfigViews(Context context) {
            mContext = context;
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClickImage(getAdapterPosition(), getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int i, String imageUri) {
//            imageView.setNum(i);
            imageView.displayImage(imageUri);
            ll_picture.addView(getPicture());
            ll_picture.addView(getPicture());
            ll_picture.addView(getPicture());

        }

        private View getPicture() {
            float span = mContext.getResources().getDimension(R.dimen.bj);

            float sw = Utils.getSrceenWidth();

            int picNum = 3;

            int itemSize = (int) ((sw - (picNum + 1) * span) / picNum);

            SampleImageView iv = new SampleImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // fake Data
            List<String> images = new ArrayList<>();
            images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646800199&di=e588dafd6e16678d08e8404c7f6a5651&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_2_1979295486D2113125476_23.jpg");
            images.add("http://v1.qzone.cc/avatar/201405/10/17/00/536deaa6c35a9512.jpg!200x200.jpg");
            images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646872650&di=8c968f968b9423051048d1eec7c5d598&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_3520253239D3803949043_21.jpg");
            images.add("http://img17.3lian.com/d/file/201702/22/1005a2e0825ffe290b3f697404ee8038.jpg");
            images.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=c89f6064a21ea8d38e227306a70b30cf/0824ab18972bd407ce9f04227f899e510eb30991.jpg");
            images.add("http://www.adquan.com/upload/20151223/1450838259813154.jpg");
            images.add("http://www.feizl.com/upload2007/2015_07/150720124522248.jpg");
            images.add("http://pic.iqshw.com/d/file/gexingqqziyuan/touxiang/2016/03/17/8581e320e98e541ed03a8fcab51068fd.jpg");
            images.add("http://www.feizl.com/upload2007/2015_07/1507201245222436.jpg");
            images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498646757823&di=ceb2ef896125f0f5ead9140c5e68cef7&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-3%2F2016030111061053440.jpg");
            images.add("http://www.feizl.com/upload2007/2015_07/1507201245222419.jpg");

            iv.displayImage(images.get(new Random().nextInt(images.size())));

            int nn = (int) span;

            layoutParams.width = itemSize;
            layoutParams.height = itemSize * 2 / 3;
            layoutParams.setMargins(nn, 0, 0, 0);
            iv.setLayoutParams(layoutParams);

            return iv;
        }
    }


}
