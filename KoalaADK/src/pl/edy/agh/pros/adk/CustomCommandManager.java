package pl.edy.agh.pros.adk;

import pl.edy.agh.pros.adk.util.ADKSendUtility;
import pl.edy.agh.pros.adk.util.KoalaCommands;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CustomCommandManager {

	private final Activity activity;
	private final ADKSendUtility sendUtility;

	// UI
	private EditText editText;
	private Button sendButton;


	public CustomCommandManager(Activity activity, ADKSendUtility sendUtility) {
		this.activity = activity;
		this.sendUtility = sendUtility;
	}

	public void init() {
		editText = (EditText) activity.findViewById(R.id.editText);
		sendButton = (Button) activity.findViewById(R.id.button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String message = editText.getText().toString();
				if (sendUtility != null) {
					sendUtility.sendMessage(KoalaCommands.TYPE_CUSTOM, KoalaCommands.prepareCommand(message));
				}
			}
		});
	}

}
