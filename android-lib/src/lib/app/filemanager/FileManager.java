package lib.app.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lib.ui.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FileManager {

	private static File currentDirectory = new File("/");
	private static List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
	private static File 				myTmpFile 		 = null;
	private static int 				myTmpOpt		 = -1;
	private static String TAG = FileManager.class.getSimpleName();

	// 浏览文件系统的根目录
	public static void browseToRoot(Activity activity) {
		browseTo(new File("/"), activity);
	}

	// 返回上一级目录
	public static void upOneLevel(Activity activity) {
		if (FileManager.currentDirectory.getParent() != null)
			FileManager.browseTo(FileManager.currentDirectory.getParentFile(),
					activity);
	}

	// 浏览指定的目录,如果是文件则进行打开操作
	public static void browseTo(final File file, Activity activity) {
		if(file.getAbsolutePath().length()==1){
			activity.setTitle(file.getAbsolutePath());
		}else{
			activity.setTitle(file.getAbsolutePath()+"/");
		}
		if (file.isDirectory()) {
			FileManager.currentDirectory = file;
			FileManager.fill(file.listFiles(),activity);
			FileManagerActivity fileManagerActivity = (FileManagerActivity)activity;
			if(fileManagerActivity.getListAdapter()!=null){
				fileManagerActivity.getListAdapter().notifyDataSetChanged();	
			}
		} else {
			FileManager.fileOptMenu(file, activity);
		}
	}

	// 打开指定文件
	protected static void openFile(File aFile, Activity activity) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(aFile.getAbsolutePath());
		// 取得文件名
		String fileName = file.getName();
		// 根据不同的文件类型来打开文件
		if (checkEndsWithInStringArray(fileName, activity.getResources()
				.getStringArray(R.array.fileEndingImage))) {
			intent.setDataAndType(Uri.fromFile(file), "image/*");
		} else if (checkEndsWithInStringArray(fileName, activity.getResources()
				.getStringArray(R.array.fileEndingAudio))) {
			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		} else if (checkEndsWithInStringArray(fileName, activity.getResources()
				.getStringArray(R.array.fileEndingVideo))) {
			intent.setDataAndType(Uri.fromFile(file), "video/*");
		} else {
			intent.setDataAndType(Uri.fromFile(file), "text/plain");
		}
		activity.startActivity(intent);
	}

	// 这里可以理解为设置ListActivity的源
	private static void fill(File[] files, Activity activity) {
		// 清空列表
		FileManager.directoryEntries.clear();

		// 添加一个当前目录的选项
		FileManager.directoryEntries.add(new IconifiedText(activity
				.getString(R.string.current_dir), activity.getResources()
				.getDrawable(R.drawable.fm_folder)));
		// 如果不是根目录则添加上一级目录项
		if (FileManager.currentDirectory.getParent() != null)
			FileManager.directoryEntries.add(new IconifiedText(activity
					.getString(R.string.up_one_level), activity.getResources()
					.getDrawable(R.drawable.fm_uponelevel)));

		Drawable currentIcon = null;
		if (files != null) {
			for (File currentFile : files) {
				// 判断是一个文件夹还是一个文件
				if (currentFile.isDirectory()) {
					currentIcon = activity.getResources().getDrawable(
							R.drawable.fm_folder);
				} else {
					// 取得文件名
					String fileName = currentFile.getName();
					// 根据文件名来判断文件类型，设置不同的图标
					if (checkEndsWithInStringArray(
							fileName,
							activity.getResources().getStringArray(
									R.array.fileEndingImage))) {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_image);
					} else if (checkEndsWithInStringArray(
							fileName,
							activity.getResources().getStringArray(
									R.array.fileEndingWebText))) {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_webtext);
					} else if (checkEndsWithInStringArray(
							fileName,
							activity.getResources().getStringArray(
									R.array.fileEndingPackage))) {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_packed);
					} else if (checkEndsWithInStringArray(
							fileName,
							activity.getResources().getStringArray(
									R.array.fileEndingAudio))) {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_audio);
					} else if (checkEndsWithInStringArray(
							fileName,
							activity.getResources().getStringArray(
									R.array.fileEndingVideo))) {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_video);
					} else {
						currentIcon = activity.getResources().getDrawable(
								R.drawable.fm_text);
					}
				}
				// 确保只显示文件名、不显示路径如：/sdcard/111.txt就只是显示111.txt
				int currentPathStringLenght = FileManager.currentDirectory
						.getAbsolutePath().length();
				
				FileManager.directoryEntries.add(new IconifiedText(currentFile
						.getAbsolutePath().substring(currentPathStringLenght),
						currentIcon));
			}
		}

		Collections.sort(FileManager.directoryEntries);

	}

	static class ItemClickListener implements OnClickListener {
		private File file;
		private Activity activity;

		public ItemClickListener(File file, Activity activity) {
			this.file = file;
			this.activity = activity;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			int which = arg1;
			if (which == 0) {
				FileManager.openFile(file, activity);
			} else if (which == 1) {
				// 自定义一个带输入的对话框由TextView和EditText构成
				final LayoutInflater factory = LayoutInflater.from(activity);
				final View dialogview = factory.inflate(R.layout.filemanager_rename, null);
				// 设置TextView的提示信息
				((TextView) dialogview.findViewById(R.id.TextView01))
						.setText("重命名");
				// 设置EditText输入框初始值
				((EditText) dialogview.findViewById(R.id.EditText01))
						.setText(file.getName());

				Builder builder = new Builder(activity);
				builder.setTitle("重命名");
				builder.setView(dialogview);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								final String fileName = ((EditText) dialogview
										.findViewById(R.id.EditText01))
										.getText().toString();
								// 点击确定之后
								String value = FileManager.getCurDirectory()
										+ "/" +fileName; 
								if (new File(value).exists()) {
									Builder builder = new Builder(activity);
									builder.setTitle("重命名");
									builder.setMessage("文件名重复，是否需要覆盖？");
									builder.setPositiveButton(
											android.R.string.ok,
											new AlertDialog.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													String str2 = FileManager.getCurDirectory()
															+ "/"+fileName;
													file.renameTo(new File(str2));
													Log.i(TAG," rename file: "+file+" to : "+str2);
													FileManager
															.browseTo(
																	FileManager.getCurDirectory(),
																	activity);
												}
											});
									builder.setNegativeButton(
											android.R.string.cancel,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.cancel();
												}
											});
									builder.setCancelable(false);
									builder.create();
									builder.show();
								} else {
									// 重命名
									boolean flag = file.renameTo(new File(value));
									Log.i(TAG," rename file: "+file+" to : "+value+" , flag: "+flag);
									FileManager.browseTo(FileManager.getCurDirectory(), activity);
								}
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						dialog.cancel();
					}
				});
				builder.show();
			} else if (which == 2) {
				Builder builder = new Builder(activity);
				builder.setTitle("删除文件");
				builder.setMessage("确定删除" + file.getName() + "？");
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (FileManager.deleteFile(file)) {
									Builder builder = new Builder(activity);
									builder.setTitle("提示对话框");
									builder.setMessage("删除成功");
									builder.setPositiveButton(
											android.R.string.ok,
											new AlertDialog.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 点击确定按钮之后
													dialog.cancel();
													FileManager
															.browseTo(
																	FileManager.getCurDirectory(),
																	activity);
												}
											});
									builder.setCancelable(false);
									builder.create();
									builder.show();
								} else {
									Builder builder = new Builder(activity);
									builder.setTitle("提示对话框");
									builder.setMessage("删除失败");
									builder.setPositiveButton(
											android.R.string.ok,
											new AlertDialog.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 点击确定按钮之后
													dialog.cancel();
												}
											});
									builder.setCancelable(false);
									builder.create();
									builder.show();
								}
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
			} else if (which == 3)// 复制
			{
				// 保存我们复制的文件目录
				myTmpFile = file;
				// 这里我们用0表示复制操作
				myTmpOpt = 0;
			} else if (which == 4)// 剪切
			{
				// 保存我们复制的文件目录
				myTmpFile = file;
				// 这里我们用0表示剪切操作
				myTmpOpt = 1;
			}
		}

	}

	// 处理文件，包括打开，重命名等操作
	public static void fileOptMenu(final File file,Activity activity) {

		// 显示操作菜单
		String[] menu = { "打开", "重命名", "删除", "复制", "剪切" };
		
		ItemClickListener listener = new ItemClickListener(file,activity);
		new AlertDialog.Builder(activity).setTitle("请选择你要进行的操作")
				.setItems(menu, listener).show();
	}

	public static File getCurDirectory() {
		return currentDirectory;
	}
	
	// 得到当前目录的绝对路劲
	public static String getCurDirectoryAbsolutepath() {
		return currentDirectory.getAbsolutePath();
	}

	// 移动文件
	public static void moveFile(String source, String destination) {
		new File(source).renameTo(new File(destination));
	}

	// 复制文件
	public static void copyFile(File src, File target) {
		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int len = bin.read(b);
			while (len != -1) {
				bout.write(b, 0, len);
				len = bin.read(b);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bin != null) {
					bin.close();
				}
				if (bout != null) {
					bout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 通过文件名判断是什么类型的文件
	private static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	// 粘贴操作
	public static void pasteFile(final Activity activity) {
		if (myTmpFile == null) {
			Builder builder = new Builder(activity);
			builder.setTitle("提示");
			builder.setMessage("没有复制或剪切操作");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		} else {
			if (myTmpOpt == 0)// 复制操作
			{
				if (new File(FileManager.getCurDirectory() + "/" + myTmpFile.getName())
						.exists()) {
					Builder builder = new Builder(activity);
					builder.setTitle("粘贴提示");
					builder.setMessage("该目录有相同的文件，是否需要覆盖？");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									copyFile(myTmpFile,
											new File(FileManager.getCurDirectory() + "/"
													+ myTmpFile.getName()));
									FileManager.browseTo(FileManager.getCurDirectory(),activity);
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				} else {
					copyFile(myTmpFile, new File(FileManager.getCurDirectory() + "/"
							+ myTmpFile.getName()));
					FileManager.browseTo(FileManager.getCurDirectory(),activity);
				}
			} else if (myTmpOpt == 1)// 粘贴操作
			{
				if (new File(FileManager.getCurDirectory() + "/" + myTmpFile.getName())
						.exists()) {
					Builder builder = new Builder(activity);
					builder.setTitle("粘贴提示");
					builder.setMessage("该目录有相同的文件，是否需要覆盖？");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									moveFile(
											myTmpFile.getAbsolutePath(),
											FileManager.getCurDirectory() + "/"
													+ myTmpFile.getName());
									FileManager.browseTo(FileManager.getCurDirectory(),activity);
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				} else {
					moveFile(myTmpFile.getAbsolutePath(), FileManager.getCurDirectory()
							+ "/" + myTmpFile.getName());
					FileManager.browseTo(FileManager.getCurDirectory(),activity);
				}
			}
		}
	}

	// 删除整个文件夹
	public static void deleteFile(Activity activity) {
		// 取得当前目录
		File tmp = new File(FileManager.currentDirectory.getAbsolutePath());
		// 跳到上一级目录
		FileManager.upOneLevel(activity);
		// 删除取得的目录
		if (deleteFolder(tmp)) {
			Builder builder = new Builder(activity);
			builder.setTitle("提示");
			builder.setMessage("删除成功");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		} else {
			Builder builder = new Builder(activity);
			builder.setTitle("提示");
			builder.setMessage("删除失败");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		FileManager.browseTo(FileManager.currentDirectory,activity);
	}

	// 新建文件夹
	public static void createFile(final Activity activity) {
		final LayoutInflater factory = LayoutInflater
				.from(activity);
		final View dialogview = factory.inflate(R.layout.filemanager_create_dir, null);
		// 设置TextView
		((TextView) dialogview.findViewById(R.id.TextView_PROM))
				.setText("请输入新建文件夹的名称！");
		// 设置EditText
		((EditText) dialogview.findViewById(R.id.EditText_PROM))
				.setText("文件夹名称...");

		Builder builder = new Builder(activity);
		builder.setTitle("新建文件夹");
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String value = ((EditText) dialogview
								.findViewById(R.id.EditText_PROM)).getText()
								.toString();
						if (FileManager.newFolder(value,activity)) {
							Builder builder = new Builder(activity);
							builder.setTitle("提示");
							builder.setMessage("新建文件夹成功");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 点击确定按钮之后,继续执行网页中的操作
											dialog.cancel();
										}
									});
							builder.setCancelable(false);
							builder.create();
							builder.show();
						} else {
							Builder builder = new Builder(activity);
							builder.setTitle("提示");
							builder.setMessage("新建文件夹失败");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 点击确定按钮之后,继续执行网页中的操作
											dialog.cancel();
										}
									});
							builder.setCancelable(false);
							builder.create();
							builder.show();
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	// 新建文件夹
	public static boolean newFolder(String file,Activity activity) {
		File dirFile = new File(currentDirectory.getAbsolutePath() + "/"
				+ file);
		try {
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();
				if (creadok) {
					FileManager.browseTo(currentDirectory,activity);
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}

	// 删除文件
	public static boolean deleteFile(File file) {
		boolean result = false;
		if (file != null) {
			try {
				File file2 = file;
				file2.delete();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	// 删除文件夹
	public static boolean deleteFolder(File folder) {
		boolean result = false;
		try {
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0) {
				if (folder.delete()) {
					result = true;
				}
			} else {
				for (int i = 0; i < childs.length; i++) {
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator
							+ childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile()) {
						if (filePath.delete()) {
							result = true;
						} else {
							result = false;
							break;
						}
					} else if (filePath.exists() && filePath.isDirectory()) {
						if (deleteFolder(filePath)) {
							result = true;
						} else {
							result = false;
							break;
						}
					}
				}
				folder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static List<IconifiedText> getDirectoryEntries(){
		return directoryEntries;
	}
}