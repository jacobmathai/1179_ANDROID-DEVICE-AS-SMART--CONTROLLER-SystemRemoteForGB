package com.kites.systemremoteforgb;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	public static Client client = null;
	public static MainActivity ma = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		                    .permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
        ma = this;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ServerSettingsFragment.newInstance())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int position = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (position == R.id.mouse) {

			fragmentManager.beginTransaction()
					.replace(R.id.container, MouseFragment.newInstance())
					.commit();
		} else if (position == R.id.keyboard) {

			fragmentManager.beginTransaction()
					.replace(R.id.container, KeyboardFragment.newInstance())
					.commit();

		} else if (position == R.id.arrow) {

			fragmentManager.beginTransaction()
					.replace(R.id.container, ArrowFragment.newInstance())
					.commit();
		} else if (position == R.id.server) {

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							ServerSettingsFragment.newInstance()).commit();
		}else if (position == R.id.exit) {
			finish();
		} 
		else {

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(position + 1))
					.commit();
		}
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
	// //////////////////////////////////////
	public static class MouseFragment extends Fragment {

		TextView tv = null;
		double px = 0;
		double py = 0;
		Button bt1,bt2=null;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static MouseFragment newInstance() {
			MouseFragment fragment = new MouseFragment();
			return fragment;
		}

		public MouseFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mouse,
					container, false);
			tv = (TextView) rootView.findViewById(R.id.textView1);

			tv.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub

					if(event.getAction()==MotionEvent.ACTION_DOWN){
						px=event.getX();
						py=event.getY();
					}
					if(Math.abs((event.getX() - px))+Math.abs((event.getY() - py))>30){
						
					String msg = (event.getX() - px) + ","
							+ (event.getY() - py);
					if(Variables.serverConnected){
						client.sendMouse((int)((event.getX() - px))+":"+(int)(event.getY() - py));
					}
					px = event.getX();
					py = event.getY();
					tv.setText(msg);
					}				
					// return true;
					// }
					// Toast.makeText(ma, msg, Toast.LENGTH_SHORT).show();
					return true;
				}
			});
			bt1=(Button)rootView.findViewById(R.id.button1);
			
			bt2=(Button)rootView.findViewById(R.id.button2);
			
			bt1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Variables.serverConnected)
						client.sendMouseClick("left");
				}
			});
			bt2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Variables.serverConnected)
					client.sendMouseClick("right");
				}
			});
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(1);

		}
	}

	// //////////////////////////////////////////////////
	public static class KeyboardFragment extends Fragment {

		EditText et = null;
		TextView tv = null;
		ToggleButton tb1,tb2,tb3 = null;
		Button bt[]=new Button[30];
		int buttonId[]=new int[]{R.id.bt1,R.id.bt2,R.id.bt3,R.id.bt4,R.id.bt5,R.id.bt6,R.id.bt7,R.id.bt8,R.id.bt9,R.id.bt10,
				R.id.bt11,R.id.bt12,R.id.bt13,R.id.bt14,R.id.bt15,R.id.bt16,R.id.bt17,R.id.bt18,R.id.bt19,R.id.bt20,
				R.id.bt21,R.id.bt22,R.id.bt23,R.id.bt24,R.id.bt25,R.id.bt26,R.id.bt27,R.id.bt28,R.id.bt29,R.id.bt30};
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static KeyboardFragment newInstance() {
			KeyboardFragment fragment = new KeyboardFragment();
			return fragment;
		}

		public KeyboardFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_keyboard,
					container, false);
			tb1 = (ToggleButton) rootView.findViewById(R.id.tb1);
			tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
				//	showNumPad(true);
				}
			});
			tb2 = (ToggleButton) rootView.findViewById(R.id.tb2);
			tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					//showNumPad(true);
					Variables.shiftFlag=arg1;
					if(arg1)tb3.setChecked(false);
				}
			});
			tb3 = (ToggleButton) rootView.findViewById(R.id.tb3);
			tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					//showNumPad(true);
					Variables.capsFlag=arg1;
					if(arg1)tb2.setChecked(false);
					
				}
			});
			for(int i=0;i<bt.length;i++){
				final int pos=i;
				bt[i]=(Button)rootView.findViewById(buttonId[i]);
				bt[i].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(Variables.serverConnected){
							String msg="";
							if(Variables.capsFlag){
								msg+="caps:"+pos;
							}
							else if(Variables.shiftFlag){
								msg+="shift:"+pos;
							}
							else{
								msg+=pos;
							}
							client.sendKeyboard(msg);
						}
					}
				});
			}
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(2);

		}
	}

	// ////////////////////////////////////////////////////
	public static class NumPadFragment extends Fragment {

		EditText et = null;
		TextView tv = null;
		ToggleButton tb1 = null;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static NumPadFragment newInstance() {
			NumPadFragment fragment = new NumPadFragment();
			return fragment;
		}

		public NumPadFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_numpad,
					container, false);
			tb1 = (ToggleButton) rootView.findViewById(R.id.tb1);
			tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					//showNumPad(false);
				}
			});
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(2);

		}
	}

	// ////////////////////////////////////////////////////
	public static class ArrowFragment extends Fragment {

		ImageView up, down, left, right = null;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static ArrowFragment newInstance() {
			ArrowFragment fragment = new ArrowFragment();
			return fragment;
		}

		public ArrowFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_arrow,
					container, false);
			up = (ImageView) rootView.findViewById(R.id.up);
			down = (ImageView) rootView.findViewById(R.id.down);
			left = (ImageView) rootView.findViewById(R.id.left);
			right = (ImageView) rootView.findViewById(R.id.right);

			up.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Toast.makeText(ma, "UP", Toast.LENGTH_SHORT).show();
					if(Variables.serverConnected){
						client.sendArrow("up");
					}
				}
			});
			down.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(Variables.serverConnected){
						client.sendArrow("down");
					}
					//Toast.makeText(ma, "DOWN", Toast.LENGTH_SHORT).show();

				}
			});
			left.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(Variables.serverConnected){
						client.sendArrow("left");
					}
					//Toast.makeText(ma, "LEFT", Toast.LENGTH_SHORT).show();
				}
			});
			right.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Toast.makeText(ma, "RIGHT", Toast.LENGTH_SHORT).show();
					if(Variables.serverConnected){
						client.sendArrow("right");
					}
				}
			});
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(3);

		}
	}

	// ////////////////////////////////////////////////////
	public static class ServerSettingsFragment extends Fragment {

		EditText et1=null;
		EditText et2=null;
		Button bt1=null;
		Button bt2=null;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static ServerSettingsFragment newInstance() {
			ServerSettingsFragment fragment = new ServerSettingsFragment();
			return fragment;
		}

		public ServerSettingsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_server,
					container, false);
			et1= (EditText)rootView.findViewById(R.id.editText1);
			et2= (EditText)rootView.findViewById(R.id.editText2);
			bt1=(Button)rootView.findViewById(R.id.button1);
			bt2=(Button)rootView.findViewById(R.id.button2);
			bt1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new Thread(){public void run(){client = new Client(et1.getText().toString(),1234);}}.start();
					Variables.serverConnected=true;
				}
			});
			
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(5);

		}
		
	}
	private CharSequence mTitle;
	
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_mouse);
			break;
		case 2:
			mTitle = getString(R.string.title_keyboard);
			break;
		case 3:
			mTitle = getString(R.string.title_arrow);

			break;
		case 5:
			mTitle = getString(R.string.title_server);

			break;
		}
	}

}
