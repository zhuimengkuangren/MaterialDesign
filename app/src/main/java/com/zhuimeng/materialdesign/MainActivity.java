package com.zhuimeng.materialdesign;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;
    private NavigationView navView;
    private Toolbar toolbar;

    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple),
            new Fruit("Banana", R.drawable.banana), new Fruit("Pear", R.drawable.pear),
            new Fruit("Grape", R.drawable.grape), new Fruit("Pineapple", R.drawable.pineapple),
            new Fruit("Strawberry", R.drawable.strawberry), new Fruit("Cherry", R.drawable.cherry),
            new Fruit("Mango", R.drawable.mango)};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter fruitAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化布局
        initView();
        //设置导航按钮
        setToolbar();
        //左侧滑动菜单点击事件
        LeftSlideMenuAction();
        //悬浮按钮点击事件
        FloatingButtonAction();
        //初始化水果数据
        initFruits();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);//设置2列
        recyclerView.setLayoutManager(gridLayoutManager);
        fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置下拉刷新进度条颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();//下拉刷新逻辑处理
            }
        });
    }

    /**
     * 下拉刷新逻辑处理
     */
    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        fruitAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    /**
     * 初始化水果数据
     */
    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 80; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //设置显示ToolBar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    }

    /**
     * 设置导航按钮
     */
    public void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    /**
     * 左侧滑动菜单点击事件
     */
    public void LeftSlideMenuAction() {
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //将左侧滑动菜单关闭
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 悬浮按钮点击事件
     */
    public void FloatingButtonAction() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Floating button clicked", Toast.LENGTH_SHORT).show();
                //比Toast更加先进的Snackbar
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolsbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "BackUp", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
