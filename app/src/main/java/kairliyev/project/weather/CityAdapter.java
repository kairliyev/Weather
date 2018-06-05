package kairliyev.project.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ListViewHolder> implements Filterable {

    private TextView nameView;
    private CityAdapterListener listener;
    private List<City> cityList;
    private List<City> cityListFiltered;
    private Context context;

    public class ListViewHolder extends RecyclerView.ViewHolder{

        public ListViewHolder(View itemView) {

            super(itemView);
            nameView=itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCitySelected(cityListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }

    public CityAdapter(Context context, List<City> cityList, CityAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cityList = cityList;
        this.cityListFiltered = cityList;
    }

    @NonNull
    @Override
    public CityAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_v1, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ListViewHolder holder, int position) {
        final City city = cityListFiltered.get(position);
        nameView.setText(city.getName());
    }

    @Override
    public int getItemCount() {
        return cityListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cityListFiltered = cityList;
                } else {
                    List<City> filteredList = new ArrayList<>();
                    for (City row : cityList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                        cityListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cityListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                cityListFiltered = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public interface CityAdapterListener {

        void onCitySelected(City city);

    }

}
