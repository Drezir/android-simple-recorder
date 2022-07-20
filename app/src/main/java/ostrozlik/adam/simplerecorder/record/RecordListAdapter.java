package ostrozlik.adam.simplerecorder.record;

import static ostrozlik.adam.simplerecorder.SimpleRecorderUtils.formatDuration;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ostrozlik.adam.simplerecorder.R;

public class RecordListAdapter extends ArrayAdapter<Record> {

    private final RecordsManager recordsManager;
    private final Context context;

    public RecordListAdapter(RecordsManager recordsManager, Context context) {
        super(context, R.layout.record_item, recordsManager.getRecords());
        this.recordsManager = recordsManager;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = resolveView(convertView, parent);
        pouplateFields((ViewHolder) view.getTag(), recordsManager.recordAtPosition(position));
        return view;
    }

    private void pouplateFields(ViewHolder viewHolder, Record record) {
        viewHolder.recordNameText.setText(record.getName());
        viewHolder.recordDurationText.setText(formatDuration(record.getDuration()));
        viewHolder.recordDateText.setText(record.getCreationTime().toString());
        viewHolder.recordSizeText.setText(Formatter.formatFileSize(context, record.getSizeInBytes()));
    }

    @NonNull
    private View resolveView(@Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            return createNewView(parent);
        }
        return convertView;
    }

    private View createNewView(@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.record_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.recordNameText = view.findViewById(R.id.recordNameText);
        viewHolder.recordDurationText = view.findViewById(R.id.recordDurationText);
        viewHolder.recordDateText = view.findViewById(R.id.recordDateText);
        viewHolder.recordSizeText = view.findViewById(R.id.recordSizeText);
        view.setTag(viewHolder);
        return view;
    }

    private static class ViewHolder {
        private TextView recordNameText;
        private TextView recordDurationText;
        private TextView recordDateText;
        private TextView recordSizeText;
    }
}
