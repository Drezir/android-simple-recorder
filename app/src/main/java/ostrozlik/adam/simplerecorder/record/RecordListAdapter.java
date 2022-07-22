package ostrozlik.adam.simplerecorder.record;

import static ostrozlik.adam.simplerecorder.SimpleRecorderUtils.formatDurationWithHours;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.Formatter;
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

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import ostrozlik.adam.simplerecorder.R;
import ostrozlik.adam.simplerecorder.record.manager.RecordManager;
import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;
import ostrozlik.adam.simplerecorder.record.player.PlayerMediatorImpl;
import ostrozlik.adam.simplerecorder.record.player.RecordPlayerManager;

public class RecordListAdapter extends BaseExpandableListAdapter {

    private final RecordManager recordManager;
    private final Context context;
    private final RecordPlayerManager recordPlayerManager;
    private final Consumer<Runnable> runOnUiThread;

    public RecordListAdapter(RecordManager recordManager, Context context, Consumer<Runnable> runOnUiThread) {
        this.recordManager = recordManager;
        this.context = context;
        this.recordPlayerManager = new RecordPlayerManager(context);
        this.runOnUiThread = runOnUiThread;
    }

    private void pouplateFields(RecordViewHolder recordViewHolder, Record record) {
        recordViewHolder.recordNameText.setText(record.getName() + "." + record.getRecordExtension().getExtension());
        recordViewHolder.recordDurationText.setText(formatDurationWithHours(record.getDuration()));
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
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        this.recordPlayerManager.playingDone();
    }

    @Override
    public int getGroupCount() {
        return this.recordManager.getRecords().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.recordManager.findByIndex(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.recordManager.findByIndex(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.recordManager.findByIndex(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return this.recordManager.findByIndex(groupPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = resolveRecordView(convertView, parent);
        pouplateFields((RecordViewHolder) view.getTag(), recordManager.findByIndex(groupPosition));
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
        final Record record = recordManager.findByIndex(index);
        prepareDeleteButton(recordDetailViewHolder.deleteButton, record);
        prepareRenameButton(recordDetailViewHolder.renameButton, record);
        preparePlayButton(recordDetailViewHolder, record);
    }

    private void preparePlayButton(RecordDetailViewHolder holder, Record record) {
        ImageButton playButton = holder.playButton;
        playButton.setOnClickListener(v -> {
            try {
                PlayerMediator mediator = createPlayerMediator(holder, record);
                holder.recordSeekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener(mediator));
                this.recordPlayerManager.playOrPause(
                        recordManager.uriToPlay(record),
                        record.getDuration(),
                        mediator
                );
            } catch (IOException e) {
                Log.e("record-play", "Error playing record", e);
            }
        });
    }

    private PlayerMediator createPlayerMediator(RecordDetailViewHolder holder, Record record) {
        return new PlayerMediatorImpl(
                holder.recordSeekBar,
                holder.recordSeekBarPassedTimeText,
                holder.recordSeekBarTimeLeftText,
                record.getDuration(),
                holder.playButton,
                this.context
        ) {
            @Override
            public void seekTo(int progress) {
                RecordListAdapter.this.runOnUiThread.accept(() -> super.seekTo(progress));
            }

            @Override
            public void setMaxSeek(Duration duration) {
                RecordListAdapter.this.runOnUiThread.accept(() -> super.setMaxSeek(duration));
            }

            @Override
            public void release() {
                RecordListAdapter.this.runOnUiThread.accept(super::release);
                RecordListAdapter.this.recordPlayerManager.reset();
            }
        };
    }

    private void prepareRenameButton(ImageButton renameButton, Record record) {
        renameButton.setOnClickListener(v -> {
            EditText editText = new EditText(this.context);
            editText.setText(record.getName());
            new AlertDialog.Builder(this.context)
                    .setTitle("Rename record")
                    .setView(editText)
                    .setPositiveButton("Ok", (dialog, which) -> recordManager.renameRecord(record, editText.getText().toString()))
                    .setNegativeButton("Cancel", (dialog, which) -> Log.i("record-rename", "Record not renamed"))
                    .show();
        });
    }

    private void prepareDeleteButton(ImageButton deleteButton, Record record) {
        deleteButton.setOnClickListener(v -> new AlertDialog.Builder(this.context)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    this.recordPlayerManager.playingDone();
                    this.recordManager.deleteRecord(record);
                })
                .setNegativeButton("No", (dialog, which) -> Log.i("record-delete", "Record not deleted"))
                .show());
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

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private final AtomicBoolean seeking;
        private final PlayerMediator mediator;

        public MyOnSeekBarChangeListener(PlayerMediator mediator) {
            this.mediator = mediator;
            seeking = new AtomicBoolean();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seeking.get()) {
                mediator.seekTo(progress);
                RecordListAdapter.this.recordPlayerManager.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            this.seeking.set(true);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            this.seeking.set(false);
        }
    }
}
