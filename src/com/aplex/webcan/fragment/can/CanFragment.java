package com.aplex.webcan.fragment.can;

import java.util.ArrayList;
import java.util.List;

import com.android.socketcan.CanSocket.CanFrame;
import com.aplex.webcan.R;
import com.aplex.webcan.base.BaseFragment;
import com.aplex.webcan.customview.CanSetupDialog;
import com.aplex.webcan.customview.MyListView;
import com.aplex.webcan.flatui.view.FlatButton;
import com.aplex.webcan.flatui.view.FlatEditText;
import com.aplex.webcan.flatui.view.FlatToggleButton;
import com.aplex.webcan.util.SPUtils;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CanFragment extends BaseFragment<CanPresenter, CanModel> implements OnClickListener, CanContract.View {
	private LinearLayout mRootView;
	private FlatToggleButton mToggle;
	private Spinner mDevice;
	private Spinner mSendMode;
	private Spinner mType;
	private Spinner mFormat;

	private FlatButton mCan0Setup;
	private FlatButton mCan1Setup;
	private FlatButton mSendBtn;
	private FlatButton mClearBtn;
	// private CanSetupDialog mCanSetupDialog;
	private MyListView mCanZeroLv;
	private MyListView mCanOneLv;

	private FlatEditText mCanIdTv;
	private FlatEditText mDataOfSendTv;
	private FlatEditText mSendTimeTv;
	private FlatEditText mSendIntvlTv;

	private int mCurrentDevicePosition;
	private int mCurrentSendModePosition;
	private int mCurrentTypePosition;
	private int mCurrentFormatTypePosition;

	private int mCurrentCan0BirratePosition;
	private int mCurrentCan1BirratePosition;
	private Can0DataAdapter mCan0DataAdapter;
	CanFrame revCan0Data;
	private List<CanFrame> mCanDatas = new ArrayList<CanFrame>();

	@Override
	protected View createView(LayoutInflater inflater) {
		initView(inflater);
		initData();
		initListener();
		return mRootView;
	}

	private void initListener() {
		mClearBtn.setOnClickListener(this);
		mToggle.setOnClickListener(this);
		mSendBtn.setOnClickListener(this);
		mCan0Setup.setOnClickListener(this);
		mCan1Setup.setOnClickListener(this);
		initSpinnerListener();
	}

	private void initSpinnerListener() {
		mDevice.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					mCurrentDevicePosition = position;
					SPUtils.setInt(getActivity(), "canDevicePosition", position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mSendMode.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					mCurrentSendModePosition = position;
					SPUtils.setInt(getActivity(), "canSendModePosition", position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mCurrentTypePosition = position;
				SPUtils.setInt(getActivity(), "canSendTypePosition", position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		mFormat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mCurrentFormatTypePosition = position;
				SPUtils.setInt(getActivity(), "canFormatTypePosition", position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void initData() {
		// can设备选择
		mCurrentDevicePosition = SPUtils.getInt(getActivity(), "canDevicePosition", 0);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button,
				mPresenter.getDevices());
		adapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mDevice.setAdapter(adapter);
		mDevice.setSelection(mCurrentDevicePosition);

		// send Mode
		mCurrentSendModePosition = SPUtils.getInt(getActivity(), "canSendModePosition", 0);
		ArrayAdapter<String> sendModeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button,
				mPresenter.getSendModes());
		sendModeAdapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mSendMode.setAdapter(sendModeAdapter);
		mSendMode.setSelection(mCurrentSendModePosition);

		// type
		mCurrentTypePosition = SPUtils.getInt(getActivity(), "canSendTypePosition", 0);
		ArrayAdapter<String> sendTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button,
				mPresenter.getSendTypes());
		sendTypeAdapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mType.setAdapter(sendTypeAdapter);
		mType.setSelection(mCurrentTypePosition);

		// format
		mCurrentFormatTypePosition = SPUtils.getInt(getActivity(), "canFormatTypePosition", 0);
		ArrayAdapter<String> formetTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_button,
				mPresenter.getCanFormats());
		formetTypeAdapter.setDropDownViewResource(R.layout.simple_flat_list_item);
		mFormat.setAdapter(formetTypeAdapter);
		mFormat.setSelection(mCurrentFormatTypePosition);

		mCan0DataAdapter = new Can0DataAdapter();
		mCanZeroLv.setAdapter(mCan0DataAdapter);

		//
		mCurrentCan0BirratePosition = getResources().getStringArray(R.array.baudrate).length - 1;
		mCurrentCan1BirratePosition = getResources().getStringArray(R.array.baudrate).length - 1;
	}

	private void initView(LayoutInflater inflater) {
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_can, null);
		mToggle = (FlatToggleButton) mRootView.findViewById(R.id.toggle);
		mDevice = (Spinner) mRootView.findViewById(R.id.can_device);
		mSendMode = (Spinner) mRootView.findViewById(R.id.send_mode);
		mType = (Spinner) mRootView.findViewById(R.id.frame_type);
		mFormat = (Spinner) mRootView.findViewById(R.id.frame_format);

		mCan0Setup = (FlatButton) mRootView.findViewById(R.id.CAN0_Setting);
		mCan1Setup = (FlatButton) mRootView.findViewById(R.id.CAN1_Setting);
		mSendBtn = (FlatButton) mRootView.findViewById(R.id.send_btn);
		mClearBtn = (FlatButton) mRootView.findViewById(R.id.clear_btn);

		mCanIdTv = (FlatEditText) mRootView.findViewById(R.id.can_id);
		mDataOfSendTv = (FlatEditText) mRootView.findViewById(R.id.data_of_send);
		mSendTimeTv = (FlatEditText) mRootView.findViewById(R.id.times);
		mSendIntvlTv = (FlatEditText) mRootView.findViewById(R.id.send_intvl);

		mCanZeroLv = (MyListView) mRootView.findViewById(R.id.can0_info);
		mCanOneLv = (MyListView) mRootView.findViewById(R.id.can1_info);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			sendData();
			break;
		case R.id.CAN0_Setting:
			showCAN0SetupDialog();
			break;
		case R.id.CAN1_Setting:
			showCAN1SetupDialog();
			break;
		case R.id.toggle:
			turnToggle(v);
		case R.id.clear_btn:
			clearData();
		default:
			break;
		}
	}

	private void clearData() {
		mCanDatas.clear();
		mCan0DataAdapter.notifyDataSetChanged();
	}

	private void turnToggle(View v) {
		// 可以用rxjava简化 用busevent
		if (mToggle.isChecked()) {
			mPresenter.turnOnToggle(
					SPUtils.getInt(getActivity(), "mCurrentCan0BirratePosition", mCurrentCan0BirratePosition),
					SPUtils.getInt(getActivity(), "mCurrentCan1BirratePosition", mCurrentCan1BirratePosition));
		} else {
			mPresenter.turnOffToggle();
		}
	}

	private void sendData() {
		if (!mToggle.isChecked()) {
			// TODO
			return;
		}
		String canIdStr = mCanIdTv.getText().toString();
		String text = mDataOfSendTv.getText().toString();
		String times = mSendTimeTv.getText().toString();
		String inval = mSendIntvlTv.getText().toString();
		text = text.replaceAll("\\s*", "");
		mPresenter.sendCanData(canIdStr, text, mCurrentTypePosition, mCurrentFormatTypePosition, times, inval);
	}

	private void showCAN1SetupDialog() {
		final CanSetupDialog.Builder builder = new CanSetupDialog.Builder(getActivity());
		builder.setMessage("CAN1 SETUP").setSpinner1("CAN mode", getResources().getStringArray(R.array.CAN_mode), 0,
				new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				}).setSpinner2("baudrate", getResources().getStringArray(R.array.baudrate),
						getResources().getStringArray(R.array.baudrate).length - 1, new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}
						})
				.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.dismiss();
					}
				}).setPositiveButton("comfirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.dismiss();

					}
				}).create().show();

	}

	private void showCAN0SetupDialog() {
		final CanSetupDialog.Builder builder = new CanSetupDialog.Builder(getActivity());
		builder.setMessage("CAN0 SETUP").setSpinner1("CAN mode", getResources().getStringArray(R.array.CAN_mode), 0,
				new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				}).setSpinner2("baudrate", getResources().getStringArray(R.array.baudrate),
						mCurrentCan0BirratePosition, new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								mCurrentCan0BirratePosition = position;
								SPUtils.setInt(getActivity(), "mCurrentCan0BirratePosition",
										mCurrentCan0BirratePosition);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}
						})
				.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.dismiss();
					}
				}).setPositiveButton("comfirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.dismiss();

					}
				}).create().show();
	}

	private class Can0DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mCanDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mCanDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.ui_can_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.index = (TextView) convertView.findViewById(R.id.can_data_list_index);
				viewHolder.direction = (TextView) convertView.findViewById(R.id.can_data_list_direction);
				viewHolder.ID = (TextView) convertView.findViewById(R.id.can_data_list_ID);
				viewHolder.frame_Type = (TextView) convertView.findViewById(R.id.can_data_list_frametype);
				viewHolder.Frame_Formate = (TextView) convertView.findViewById(R.id.can_data_list_frame_formate);
				viewHolder.Data_len = (TextView) convertView.findViewById(R.id.can_data_list_datalen);
				viewHolder.Data = (TextView) convertView.findViewById(R.id.can_data_list_data);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			CanFrame canFrame = mCanDatas.get(position);
			viewHolder.index.setText(position + 1 + "");
			viewHolder.direction.setText(canFrame.rx_tx == CanFrame.RX ? "RX" : "TX");
			viewHolder.ID.setText(intToHexString((canFrame.getCanId().isSetEFFSFF() ? canFrame.getCanId().getCanId_EFF()
					: canFrame.getCanId().getCanId_SFF()), 8));
			viewHolder.frame_Type.setText(canFrame.getCanId().isSetEFFSFF() ? "EFF" : "SFF");
			viewHolder.Frame_Formate.setText(canFrame.getCanId().isSetRTR() ? "REMOTE" : "DATA");
			viewHolder.Data_len.setText(canFrame.getData().length + "");
			viewHolder.Data.setText(canFrame.getCanId().isSetRTR() ? ""
					: intArrayToHexString(getUnsignedByteArray(canFrame.getData())));

			return convertView;
		}
	}

	static class ViewHolder {
		TextView index;
		TextView direction;
		TextView ID;
		TextView frame_Type;
		TextView Frame_Formate;
		TextView Data_len;
		TextView Data;
	}

	/**
	 * @Title:intToHexString @Description:10进制数字转成16进制 @param a 转化数据 @param len
	 *                       占用字节数 @return @throws
	 */
	private static String intToHexString(int a, int len) {
		String hexString = Integer.toHexString(a);
		int b = len - hexString.length();
		if (b > 0) {
			for (int i = 0; i < b; i++) {
				hexString = "0" + hexString;
			}
		}
		return hexString;
	}

	private static String intArrayToHexString(int[] arr) {
		String formatHexString = "";
		for (int i = 0; i < arr.length; i++) {
			// Log.e("candata", arr[i]+"");
			formatHexString = formatHexString + intToHexString(arr[i], 2);
			if (i != arr.length - 1) {
				formatHexString = formatHexString + " ";
			}
		}
		return formatHexString;
	}

	public int[] getUnsignedByteArray(byte[] data) {
		int[] copyData = new int[data.length];
		// 将data字节型数据转换为0~255 (0xFF 即BYTE)。
		for (int i = 0; i < data.length; i++) {
			copyData[i] = data[i] & 0x0FF;
		}
		return copyData;
	}

	@Override
	public void showFormatError() {
		Toast.makeText(getActivity(), getResources().getString(R.string.formaterror), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showDataError() {
		Toast.makeText(getActivity(), getResources().getString(R.string.dataerror), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showList(CanFrame revCan0Data) {
		if (revCan0Data != null) {
			mCanDatas.add(revCan0Data);
		}
		mCan0DataAdapter.notifyDataSetChanged();
		mCanZeroLv.setSelection(mCanDatas.size());
	}
}
