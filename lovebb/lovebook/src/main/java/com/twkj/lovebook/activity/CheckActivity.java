package com.twkj.lovebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twkj.lovebook.R;
import com.twkj.lovebook.adapter.CheckRecyclerAdapter;
import com.twkj.lovebook.bean.DraftBookContent;
import com.twkj.lovebook.bean.DraftBookPage;
import com.twkj.lovebook.dao.DraftBookContentDao;
import com.twkj.lovebook.dao.DraftBookPageDao;
import com.twkj.lovebook.helper.DefaultItemTouchHelpCallback;
import com.twkj.lovebook.helper.DefaultItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wht on 2016/11/4.
 */

public class CheckActivity extends Activity {
    private RecyclerView recyclerView;
    private CheckRecyclerAdapter adapter;
    private DefaultItemTouchHelper itemTouchHelper;
    private List<DraftBookPage> userInfoList=new ArrayList<DraftBookPage>();
    private CheckRecyclerAdapter.OnItemClickListener clickListener = new CheckRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(CheckActivity.this, "第" + position + "被点击", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(CheckActivity.this,WriteABookActivity.class);
            intent.putExtra("checkPosition",position);
            intent.putExtra("EditorOrBookShelf","bookShelf");
            //传递区分那本书的本地数据库bookid
            intent.putExtra("aimBookid",getIntent().getIntExtra("aimBookid",0));
            startActivity(intent);
            finish();

        }
    };

    DraftBookPageDao draftBookPageDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        draftBookPageDao =new DraftBookPageDao();
        //这里bookid取从那本书进来的id
        userInfoList=  draftBookPageDao.selectorByBookId(getIntent().getIntExtra("aimBookid",0));


//        for (int i=0;i<40;i++){
//            //这一步模拟从数据库取数据的方法
//            int index=i+1;
//            userInfoList.add("item"+index);
//        }

        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager girdLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(girdLayoutManager);

        adapter = new CheckRecyclerAdapter(this,userInfoList);
        adapter.setOnItemClickListener(clickListener);
        recyclerView.setAdapter(adapter);

        // 把ItemTouchHelper和itemTouchHelper绑定
        itemTouchHelper = new DefaultItemTouchHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setItemTouchHelper(itemTouchHelper);

        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);
    }

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener = new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
        @Override
        public void onSwiped(int adapterPosition) {
//            if (userInfoList != null) {
//                userInfoList.remove(adapterPosition);
//                adapter.notifyItemRemoved(adapterPosition);
//            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (userInfoList != null) {
                // 更换数据源中的数据Item的位置
                Log.i("============","写入之前的srcPosition"+userInfoList.get(srcPosition).getBookPage());
                Log.i("============","写入之前的tarPosition"+userInfoList.get(targetPosition).getBookPage());
                int aimbookid=getIntent().getIntExtra("aimBookid",0);
//                if(srcPosition)
                List<Integer> ooo=new ArrayList<>();
                List<DraftBookPage> cantEditList=draftBookPageDao.selectorCannotEditPageByBookId(aimbookid);
                for (int i=0;i<cantEditList.size();i++){
                        ooo.add(cantEditList.get(i).getBookPage());
                    Log.i("============","不可编辑的页面的页数啊"+cantEditList.get(i).getBookPage());
                }
                if ( ooo.contains(srcPosition)){
                    Toast.makeText(CheckActivity.this,"含有不可拖动的页面", Toast.LENGTH_LONG).show();
                    return  true;
                }
                if ( ooo.contains(targetPosition)){
                    Toast.makeText(CheckActivity.this,"含有不可拖动的页面", Toast.LENGTH_LONG).show();
                    return true;
                }



                Collections.swap(userInfoList, srcPosition, targetPosition);

//                int mid=srcPosition;
                // 更新UI中的Item的位置，主要是给用户看到交互效果
//                adapter.notifyItemMoved(srcPosition, targetPosition);

//                Log.i("============","写入之前的srcPosition"+userInfoList.get(srcPosition).getBookPage());
                userInfoList.get(srcPosition).setBookPage(srcPosition);
                Log.i("============","写入之后的srcPosition"+userInfoList.get(srcPosition).getBookPage());
//
//                Log.i("============","写入之前的tarPosition"+userInfoList.get(targetPosition).getBookPage());
                userInfoList.get(targetPosition).setBookPage(targetPosition);
                Log.i("============","写入之后的tarPosition"+userInfoList.get(targetPosition).getBookPage());

                draftBookPageDao.updatePage(userInfoList.get(targetPosition));
                draftBookPageDao.updatePage(userInfoList.get(srcPosition));

                //更新了缩略图的里的东西
                DraftBookPage src =draftBookPageDao.selectorByBookIdAndPage(aimbookid,targetPosition);
                src.setBookPage(srcPosition);
                DraftBookPage target=draftBookPageDao.selectorByBookIdAndPage(aimbookid,srcPosition);
                src.setBookPage(targetPosition);

                draftBookPageDao.updatePage(src);
                draftBookPageDao.updatePage(target);
//                draftBookPageDao.updatePageByBookIdAndBookPage(userInfoList.get(srcPosition).id,targetPosition);
//                draftBookPageDao.updatePageByBookIdAndBookPage(userInfoList.get(targetPosition).id,srcPosition);
                //然后更新page里面的页面的所有子内容
                DraftBookContentDao draftBookContentDao=new DraftBookContentDao();
                List<DraftBookContent> list=draftBookContentDao.selectorByBookIdAndBookPage(aimbookid,targetPosition);
                List<DraftBookContent> list2=draftBookContentDao.selectorByBookIdAndBookPage(aimbookid,srcPosition);
                for (int i=0;i<list.size();i++){
                    list.get(i).setBookPage(srcPosition);
                    draftBookContentDao.update(list.get(i));
                }
                for (int i=0;i<list2.size();i++){
                    list2.get(i).setBookPage(targetPosition);
                    draftBookContentDao.update(list2.get(i));
                }

                adapter.notifyDataSetChanged();

//                 draftBookPageDao.updatePage(pa ge);
                Log.i("ccc","src的的位置"+srcPosition+"=====targetPostio目标的为值"+targetPosition);

                return true;
            }
            return false;
        }
    };

    }
