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

import java.util.List;

import app.ohmysaudi.themealdb.MainActivity;
import app.ohmysaudi.themealdb.R;
import app.ohmysaudi.themealdb.api.RetrofitClient;
import app.ohmysaudi.themealdb.models.Category;
import app.ohmysaudi.themealdb.models.CategoryResponce;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.mainProgress)
    ProgressBar progressBar;

    private Context context;
    private List<Category> categories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Category");

        Call<CategoryResponce> call = RetrofitClient.getInstance().getApi().getCategories();
        call.enqueue(new Callback<CategoryResponce>() {
            @Override
            public void onResponse(Call<CategoryResponce> call, Response<CategoryResponce> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        recyclerView.setVisibility(View.VISIBLE);
                        categories = response.body().getCategories();
                        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(context, categories, new CategoriesRecyclerViewAdapter.CategoriesItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                Fragment fragment = new RecipeFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("CASE_FRAGMENT", 1); // Category Clicked Recipes
                                bundle.putString("CLICKED_NAME", categories.get(position).getStrCategory());
                                fragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();


                            }
                        });
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Responce in null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Responce isnot Successfull! Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponce> call, Throwable t) {
                Toast.makeText(context, "Failure! Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }











    /*
    * Category Adapter
    * */
    public static class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.MyViewHolder>  {

        private List<Category> categories;
        private Context context;
        private CategoriesItemClickListener listener;

        public CategoriesRecyclerViewAdapter(Context context, List<Category> categories, CategoriesItemClickListener listener) {
            this.context = context;
            this.categories = categories;
            this.listener = listener;
        }



        //////////////////////////
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_category_list, viewGroup, false);
            final MyViewHolder holder = new MyViewHolder(view);
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
            myViewHolder.categoryTitle.setText(categories.get(i).getStrCategory());
            myViewHolder.categoryDescription.setText(categories.get(i).getStrCategoryDescription());
            Glide.with(context)
                    .load(categories.get(i).getStrCategoryThumb())
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
                    .into(myViewHolder.categoryImage);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }


        ////////////////////////////

        /*
         * Customer Vew Holder
         * */

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView categoryImage;
            private TextView categoryTitle;
            private TextView categoryDescription;
            private ProgressBar progressBar;

            public MyViewHolder(View view) {
                super(view);
                categoryImage = view.findViewById(R.id.catergoryImg);
                categoryTitle =view.findViewById(R.id.categoryTitle);
                categoryDescription = view.findViewById(R.id.categoryDescription);
                progressBar = view.findViewById(R.id.progress);
            }
        }

        // Listener

        public interface CategoriesItemClickListener {
            public void onItemClick(View v, int position);
        }



        /*
         * End of the document
         * */
    }



}
