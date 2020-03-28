package app.ohmysaudi.themealdb.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import app.ohmysaudi.themealdb.MainActivity;
import app.ohmysaudi.themealdb.R;
import app.ohmysaudi.themealdb.api.RetrofitClient;
import app.ohmysaudi.themealdb.models.DetailedListOfMealsResponce;
import app.ohmysaudi.themealdb.models.FilteredResponce;
import app.ohmysaudi.themealdb.models.ListItemLink;
import app.ohmysaudi.themealdb.models.Meal;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.mainProgress)
    ProgressBar progressBar;
    @BindView(R.id.noitemLayout)
    ViewGroup noitemLayout;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();


        if (getArguments() == null){
            ((MainActivity) getActivity()).setDrawerEnabled(false);
            Toast.makeText(context, "No Aurguments!", Toast.LENGTH_SHORT).show();
            return view;
        }

        switch (getArguments().getInt("CASE_FRAGMENT")) {
            case 0: // Latest Recipes
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Recipe");
                Call<DetailedListOfMealsResponce> latestMealsCall = RetrofitClient.getInstance().getApi().getLatestMeals();
                latestMealsCall.enqueue(new Callback<DetailedListOfMealsResponce>() {
                    @Override
                    public void onResponse(Call<DetailedListOfMealsResponce> call, Response<DetailedListOfMealsResponce> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                final List<ListItemLink> mealsList = convertit(response.body().getMeals());
                                MealsRecyclerViewAdapter adapter = new MealsRecyclerViewAdapter(context, mealsList, new MealsRecyclerViewAdapter.MealsItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        Fragment fragment = new MealDetailFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("MEAL_ID", mealsList.get(position).getIdMeal());
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();

                                    }
                                });
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);


                            } else {
                                Toast.makeText(context, "Responce in null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Responce isnot Successfull! Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailedListOfMealsResponce> call, Throwable t) {

                    }
                });
                break;
            case 1: // Category Clicked Recipes
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("CLICKED_NAME", ""));
                ((MainActivity) getActivity()).setDrawerEnabled(false);
                Call<FilteredResponce> mealsByCategoryCall = RetrofitClient.getInstance().getApi().getItemsFilterByCategory(getArguments().getString("CLICKED_NAME", ""));
                mealsByCategoryCall.enqueue(new Callback<FilteredResponce>() {
                    @Override
                    public void onResponse(Call<FilteredResponce> call, Response<FilteredResponce> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                final List<ListItemLink> mealsList = response.body().getListofLinks();
                                MealsRecyclerViewAdapter adapter = new MealsRecyclerViewAdapter(context, mealsList, new MealsRecyclerViewAdapter.MealsItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        Fragment fragment = new MealDetailFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("MEAL_ID", mealsList.get(position).getIdMeal());
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();


                                    }
                                });
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(context, "Responce in null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Responce isnot Successfull! Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FilteredResponce> call, Throwable t) {
                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2: // Favorite Recipes
                noitemLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            case 3: // Search Recipes
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Search: "+getArguments().getString("KEY_SEARCH_TEXT"));
                ((MainActivity) getActivity()).setDrawerEnabled(false);
                MainActivity.search.setVisibility(View.GONE);
                Call<DetailedListOfMealsResponce> mealByNameCall = RetrofitClient.getInstance().getApi().findItemByName(getArguments().getString("KEY_SEARCH_TEXT"));
                mealByNameCall.enqueue(new Callback<DetailedListOfMealsResponce>() {
                    @Override
                    public void onResponse(Call<DetailedListOfMealsResponce> call, Response<DetailedListOfMealsResponce> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body() != null){
                                recyclerView.setVisibility(View.VISIBLE);
                                final List<ListItemLink> mealsList = convertit(response.body().getMeals());
                                MealsRecyclerViewAdapter adapter = new MealsRecyclerViewAdapter(context, mealsList, new MealsRecyclerViewAdapter.MealsItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                        Fragment fragment = new MealDetailFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("MEAL_ID", mealsList.get(position).getIdMeal());
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();

                                    }
                                });
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(context, "Responce in null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Responce isnot Successfull! Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailedListOfMealsResponce> call, Throwable t) {
                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

        return view;
    }

    private List<ListItemLink> convertit(List<Meal> meals) {
        ArrayList<ListItemLink> list = new ArrayList<>();
        for (int i = 0; i < meals.size(); i++) {
            ListItemLink obj = new ListItemLink();
            obj.setIdMeal(meals.get(i).getIdMeal() + "");
            obj.setStrMeal(meals.get(i).getStrMeal());
            obj.setStrMealThumb(meals.get(i).getStrMealThumb());
            list.add(obj);
        }
        return list;
    }





    /*
    * Meals RecyclerView Adapter
    * */

    public static class MealsRecyclerViewAdapter extends RecyclerView.Adapter<MealsRecyclerViewAdapter.MyViewHolder>{
        private List<ListItemLink> listItemLinks;
        private Context context;
        private MealsItemClickListener listener;

        public MealsRecyclerViewAdapter(Context context, List<ListItemLink> listItemLinks, MealsItemClickListener listener) {
            this.context = context;
            this.listItemLinks = listItemLinks;
            this.listener = listener;
        }

        //////////////////////////////////////////
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_meals_list, viewGroup, false);
            final MealsRecyclerViewAdapter.MyViewHolder holder = new MealsRecyclerViewAdapter.MyViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, holder.getPosition());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int postition) {
            myViewHolder.itemTitle.setText(listItemLinks.get(postition).getStrMeal());
            Glide.with(context)
                    .load(listItemLinks.get(postition).getStrMealThumb())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            myViewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            myViewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(myViewHolder.itemImage);
        }

        @Override
        public int getItemCount() {
            return listItemLinks.size();
        }

        ////////////////////////////////
        ////////////////////////////
        // Custome View Holder Starts here.
        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView itemImage;
            private TextView itemTitle;
            private ProgressBar progressBar;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.mealImage);
                itemTitle = itemView.findViewById(R.id.mealTitle);
                progressBar = itemView.findViewById(R.id.progressBar);
            }
        }


        // Meals Item Click Listnener

        public interface MealsItemClickListener {
            public void onItemClick(View v, int position);
        }

    }


}
