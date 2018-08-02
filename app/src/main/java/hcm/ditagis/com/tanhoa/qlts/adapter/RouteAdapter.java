package hcm.ditagis.com.tanhoa.qlts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import hcm.ditagis.com.tanhoa.qlts.R;
import hcm.ditagis.com.tanhoa.qlts.tools.Distance;
import hcm.ditagis.com.tanhoa.qlts.tools.Duration;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class RouteAdapter extends ArrayAdapter<RouteAdapter.Item> {
    private Context mContext;
    private List<Item> items;

    public RouteAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_route, null);
        }
        Item item = items.get(position);

        TextView txtInstruction = (TextView) convertView.findViewById(R.id.txt_route_instruction);
        txtInstruction.setText(item.getHtml_instructions());

        TextView txtSubInstruction = (TextView) convertView.findViewById(R.id.txt_route_sub_instruction);
        if (item.getHtml_sub_instructions() != null)
            txtSubInstruction.setText(item.getHtml_sub_instructions());
        else
            txtSubInstruction.setVisibility(View.GONE);
        TextView txtDistance = (TextView) convertView.findViewById(R.id.txt_route_distance);
        //todo
        txtDistance.setText(item.getDistance().value + "m");
        return convertView;
    }


    public static class Item {
        public Distance distance;
        public Duration duration;
        public LatLng endLocation;
        public LatLng startLocation;
        public String html_instructions;
        public String html_sub_instructions;
        public String travel_mode;

        public Item() {
        }

        public String getHtml_sub_instructions() {
            return html_sub_instructions;
        }

        public void setHtml_sub_instructions(String html_sub_instructions) {
            this.html_sub_instructions = html_sub_instructions;
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public LatLng getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(LatLng endLocation) {
            this.endLocation = endLocation;
        }

        public LatLng getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(LatLng startLocation) {
            this.startLocation = startLocation;
        }

        public String getHtml_instructions() {
            return html_instructions;
        }

        public void setHtml_instructions(String html_instructions) {
            this.html_instructions = html_instructions;
        }

        public String getTravel_mode() {
            return travel_mode;
        }

        public void setTravel_mode(String travel_mode) {
            this.travel_mode = travel_mode;
        }
    }
}
