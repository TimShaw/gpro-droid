package lib.app.filemanager;

import java.io.File;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FileManagerActivity extends Activity
{
	
public Activity me = this;
	
	private IconifiedTextListAdapter listAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setContentView(R.layout.filemanager_layout);
		ListView listView = (ListView)findViewById(R.id.listView);
		
		FileManager.browseToRoot(this);
		
		listAdapter = new IconifiedTextListAdapter(this);
		// 将表设置到ListAdapter中
		listAdapter.setListItems(FileManager.getDirectoryEntries());
		// 为ListActivity添加一个ListAdapter
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 取得选中的一项的文件名
				String selectedFileString = FileManager.getDirectoryEntries().get(position).getText();
				
				if (selectedFileString.equals(getString(R.string.current_dir)))
				{
					//如果选中的是刷新
					FileManager.browseTo(FileManager.getCurDirectory(),me);
				}
				else if (selectedFileString.equals(getString(R.string.up_one_level)))
				{
					//返回上一级目录
					FileManager.upOneLevel(me);
				}
				else
				{
							
					File clickedFile = null;
					clickedFile = new File(FileManager.getCurDirectoryAbsolutepath()+ "/"+FileManager.getDirectoryEntries().get(position).getText());
					if(clickedFile != null){
						FileManager.browseTo(clickedFile,me);
					}
				}
			}
			
		});
		listView.setSelection(0);
	}
	
	public IconifiedTextListAdapter getListAdapter(){
		return listAdapter;
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "新建目录").setIcon(R.drawable.fm_addfolderr);
		menu.add(0, 1, 0, "删除目录").setIcon(R.drawable.fm_delete);
		menu.add(0, 2, 0, "粘贴文件").setIcon(R.drawable.fm_paste);
		menu.add(0, 3, 0, "根目录").setIcon(R.drawable.fm_goroot);
		menu.add(0, 4, 0, "上一级").setIcon(R.drawable.fm_uponelevel);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case 0:
				FileManager.createFile(me);
				break;
			case 1:
				//注意：删除目录，谨慎操作，该例子提供了
				//deleteFile（删除文件）和deleteFolder（删除整个目录）
				FileManager.deleteFile(me);
				break;
			case 2:
				FileManager.pasteFile(me);
				break;
			case 3:
				FileManager.browseToRoot(me);
				break;
			case 4:
				FileManager.upOneLevel(me);
				break;
		}
		return false;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		if(FileManager.getCurDirectoryAbsolutepath().equals("/")){
			super.onBackPressed();
		}else{
			FileManager.upOneLevel(me);
		}
	}
	
}
