package ostrozlik.adam.simplerecorder.record;

import static ostrozlik.adam.simplerecorder.SimpleRecorderUtils.formatDuration;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ostrozlik.adam.simplerecorder.R;
import ostrozlik.adam.simplerecorder.record.manager.RecordsManager;

public class RecordListAdapter extends BaseExpandableListAdapter {

    private final RecordsManager recordsManager;
    private final Context context;

    public RecordListAdapter(RecordsManager recordsManager, Context context) {
        this.recordsManager = recordsManager;
        this.context = context;
    }

    private void pouplateFields(RecordViewHolder recordViewHolder, Record record) {
        recordViewHolder.recordNameText.setText(record.getName() + "." + record.getRecordExtension().getExtension());
        recordViewHolder.recordDurationText.setText(formatDuration(record.getDuration()));
        recordViewHolder.recordDateText.setText(record.getCreationTime().toString());
        recordViewHolder.recordSizeText.setText(Formatter.formatFileSize(context, record.getSizeInBytes()));
    }

    private View resolveRecordView(@Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            return createNewRecordView(parent);
        }
        return convertView;
    }

    private View createNewRecordView(@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.record_item, parent, false);
        RecordViewHolder recordViewHolder = new RecordViewHolder();
        recordViewHolder.recordNameText = view.findViewById(R.id.recordNameText);
        recordViewHolder.recordDurationText = view.findViewById(R.id.recordDurationText);
        recordViewHolder.recordDateText = view.findViewById(R.id.recordDateText);
        recordViewHolder.recordSizeText = view.findViewById(R.id.recordSizeText);
        view.setTag(recordViewHolder);
        return view;
    }

    private View createNewRecordDetailView(@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.record_item_detail, parent, false);
        RecordDetailViewHolder recordDetailViewHolder = new RecordDetailViewHolder();
        recordDetailViewHolder.recordSeekBar = view.findViewById(R.id.recordSeekBar);
        recordDetailViewHolder.playButton = view.findViewById(R.id.playButton);
        recordDetailViewHolder.renameButton = view.findViewById(R.id.renameButton);
        recordDetailViewHolder.deleteButton = view.findViewById(R.id.deleteButton);
        recordDetailViewHolder.recordSeekBarPassedTimeText = view.findViewById(R.id.recordSeekBarPassedTimeText);
        recordDetailViewHolder.recordSeekBarTimeLeftText = view.findViewById(R.id.recordSeekBarTimeLeftText);
        view.setTag(recordDetailViewHolder);
        return view;
    }

    @Override
    public int getGroupCount() {
        return this.recordsManager.getRecords().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.recordsManager.recordAtPosition(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.recordsManager.recordAtPosition(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.recordsManager.recordAtPosition(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return this.recordsManager.recordAtPosition(groupPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = resolveRecordView(convertView, parent);
        pouplateFields((RecordViewHolder) view.getTag(), recordsManager.recordAtPosition(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View result = convertView;
        if (result == null) {
            result = createNewRecordDetailView(parent);
        }
        createRecordDetailBehaviour((RecordDetailViewHolder) result.getTag(), groupPosition);
        return result;
    }

    private void createRecordDetailBehaviour(RecordDetailViewHolder recordDetailViewHolder, int index) {
        recordDetailViewHolder.deleteButton.setOnClickListener(v -> new AlertDialog.Builder(this.context)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> recordsManager.deleteAtPosition(index))
                .setNegativeButton("No", (dialog, which) -> Log.i("record-delete", "Record not deleted"))
                .show());
        recordDetailViewHolder.renameButton.setOnClickListener(v -> {
            EditText editText = new EditText(this.context);
            editText.setText(recordsManager.recordAtPosition(index).getName());
            new AlertDialog.Builder(this.context)
                    .setTitle("Rename record")
                    .setView(editText)
                    .setPositiveButton("Ok", (dialog, which) -> recordsManager.renameRecord(index, editText.getText().toString()))
                    .setNegativeButton("Cancel", (dialog, which) -> Log.i("record-rename", "Record not renamed"))
                    .show();
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class RecordViewHolder {
        private TextView recordNameText;
        private TextView recordDurationText;
        private TextView recordDateText;
        private TextView recordSizeText;
    }
    private static class RecordDetailViewHolder {
        private SeekBar recordSeekBar;
        private ImageButton playButton;
        private ImageButton renameButton;
        private ImageButton deleteButton;
        private TextView recordSeekBarPassedTimeText;
        private TextView recordSeekBarTimeLeftText;
    }
}
