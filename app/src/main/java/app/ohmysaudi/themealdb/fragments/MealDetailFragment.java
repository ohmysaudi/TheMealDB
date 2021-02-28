package app.ohmysaudi.themealdb.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import app.ohmysaudi.themealdb.models.Ingredients;
import app.ohmysaudi.themealdb.models.Meal;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MealDetailFragment extends Fragment {

    @BindView(R.id.mealImage)
    ImageView mealImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.mealTitle)
    TextView mealTitle;
    @BindView(R.id.categoryText)
    TextView categoryText;
    @BindView(R.id.areaImage)
    ImageView areaImage;
    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.measuringRecyclerView)
    RecyclerView measuringRecyclerView;
    @BindView(R.id.instructionsText)
    TextView instructionsText;

    private Meal meal;
    private List<Ingredients> ingredients;
    private List<String> measures;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_detail, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();

        ((MainActivity) getActivity()).setDrawerEnabled(false);


        if (getArguments() != null) {
            if (!getArguments().getString("MEAL_ID", "").isEmpty()) {
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("ID: " + getArguments().getString("MEAL_ID", ""));
                Call<DetailedListOfMealsResponce> call = RetrofitClient.getInstance().getApi().findItemByID(getArguments().getString("MEAL_ID"));
                call.enqueue(new Callback<DetailedListOfMealsResponce>() {
                    @Override
                    public void onResponse(Call<DetailedListOfMealsResponce> call, Response<DetailedListOfMealsResponce> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                setupData(response);
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
            }
        } else {
            Call<DetailedListOfMealsResponce> call = RetrofitClient.getInstance().getApi().getRandomMeal();
            call.enqueue(new Callback<DetailedListOfMealsResponce>() {
                @Override
                public void onResponse(Call<DetailedListOfMealsResponce> call, Response<DetailedListOfMealsResponce> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            ((MainActivity) getActivity()).getSupportActionBar().setTitle("ID: " + response.body().getMeals().get(0).getIdMeal());
                            setupData(response);
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
        }

        return view;
    }

    private void setupData(Response<DetailedListOfMealsResponce> response) {
        meal = response.body().getMeals().get(0);
        prepareIngredients();
        prepareMeasurings();
        mealTitle.setText(meal.getStrMeal());
        categoryText.setText(meal.getStrCategory());
        instructionsText.setText(meal.getStrInstructions());
        Glide.with(context)
                .load(meal.getStrMealThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mealImage);
    }

    private void prepareIngredients() {
        String base_URL = "https://www.themealdb.com/images/ingredients/";
        String url_END = "-small.png";
        String ingrName = "";

        ingredients = new ArrayList<>();


        ingrName = meal.getStrIngredient1();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient2();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient3();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient4();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient5();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient6();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient7();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient8();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient9();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient10();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient11();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient12();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient13();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient14();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient15();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient16();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient17();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient18();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient19();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }
        ingrName = meal.getStrIngredient20();
        if (ingrName != null) {
            if (!ingrName.isEmpty()) {
                ingredients.add(new Ingredients(ingrName, base_URL + ingrName + url_END));
            }
        }

        IngredientRecyclerViewAdapter adapter = new IngredientRecyclerViewAdapter(ingredients, context, new IngredientRecyclerViewAdapter.IngredientsItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        ingredientRecyclerView.setLayoutManager(layoutManager);
        ingredientRecyclerView.setAdapter(adapter);
    }

    private void prepareMeasurings() {
        measures = new ArrayList<>();
        String measureTitle = "";

        measureTitle = meal.getStrMeasure1();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure2();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure3();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure4();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure5();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure6();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure7();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure8();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure9();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure10();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure11();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure12();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure13();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure14();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure15();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure16();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure17();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure18();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure19();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }
        measureTitle = meal.getStrMeasure20();
        if (measureTitle != null) {
            if (!measureTitle.isEmpty()) {
                measures.add(measureTitle);
            }
        }

        MeasuringRecyclerViewAdapter adapter = new MeasuringRecyclerViewAdapter(context, measures, ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        measuringRecyclerView.setLayoutManager(layoutManager);
        measuringRecyclerView.setAdapter(adapter);

    }





    /*
     * Ingredient RecyclerView Adapter
     * */

    public static class IngredientRecyclerViewAdapter
            extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.MyViewHolder> {

        private final List<Ingredients> ingredients;
        private final Context context;
        private final IngredientsItemClickListener listener;

        public IngredientRecyclerViewAdapter(List<Ingredients> ingredients, Context context, IngredientsItemClickListener listener) {
            this.ingredients = ingredients;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_ingredient_list, viewGroup, false);
            final IngredientRecyclerViewAdapter.MyViewHolder holder = new IngredientRecyclerViewAdapter.MyViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, holder.getPosition());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            myViewHolder.ingrTitle.setText(ingredients.get(i).getIngrTitle());
            Glide.with(context)
                    .load(ingredients.get(i).getIngrImage())
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
                    .into(myViewHolder.ingrImage);
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        /*
         * Customer View Holder
         *
         * */

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final ImageView ingrImage;
            private final TextView ingrTitle;
            private final ProgressBar progressBar;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ingrImage = itemView.findViewById(R.id.ingrImage);
                ingrTitle = itemView.findViewById(R.id.ingrTitle);
                progressBar = itemView.findViewById(R.id.progressBar);
            }
        }


        // Ingredient Click Listener

        public interface IngredientsItemClickListener {
            void onItemClick(View v, int position);
        }
    }




    /*
     * Measuring RecyclerView Adapter
     * */


    public class MeasuringRecyclerViewAdapter
            extends RecyclerView.Adapter<MeasuringRecyclerViewAdapter.MyViewHolder> {
        private final Context context;
        private final List<String> measures;
        private final List<Ingredients> ingredients;

        public MeasuringRecyclerViewAdapter(Context context, List<String> measures, List<Ingredients> ingredients) {
            this.context = context;
            this.measures = measures;
            this.ingredients = ingredients;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_measuring_list, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.measuringText.setText(String.format("%s: %s", ingredients.get(i).getIngrTitle(), measures.get(i)));
        }

        @Override
        public int getItemCount() {
            /*
             * NOTE:
             * Because Sometimes
             * Ingredients & Measures are
             * Not of same length.
             * So Chose small one to avoid crashes.
             * */
            return (ingredients.size() < measures.size()) ? ingredients.size() : measures.size();
        }

        /*
         *  Custom View Holder
         * */
        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView measuringText;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                measuringText = itemView.findViewById(R.id.measuringText);
            }
        }
    }

}
