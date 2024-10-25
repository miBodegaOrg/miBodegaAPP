package com.mibodega.mystore.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Conference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainNavigationActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.CategoryResponseWithProducts;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;
import com.mibodega.mystore.shared.adapters.SubcategoryView;
import com.mibodega.mystore.views.products.ProductDetailActivity;
import com.mibodega.mystore.views.products.ProductEditActivity;
import com.mibodega.mystore.views.supplier.SupplierRegisterActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductsFragment extends Fragment {


    private GridLayoutManager glm;
    private ArrayList<ProductResponse> productlist = new ArrayList<>();
    private ArrayList<String> arrayListCategoriesChecked = new ArrayList<>();
    private ArrayList<SubcategoryView> arrayListSubCategoriesView = new ArrayList<>();
    private PagesProductResponse pagesProductResponse;
    private Config  config = new Config();
    private RecyclerView recyclerView;
    private FloatingActionButton btnMoveToAddProduct;
    private LinearLayout ly_categoriesContainer;
    private MaterialCardView viewCategory;
    private ImageButton btn_toggleCategory;
    private int NUMBER_SIZE_PAGINATION=20;
    private TextView tv_messageNotFound;

    private TextInputEditText edt_searchText;

    private LinearLayout linearLayoutSubcategoryViews;
    private View _root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        _root =root;
        recyclerView = root.findViewById(R.id.Rv_productlist_product);
        initProductsData(root);

        btnMoveToAddProduct = root.findViewById(R.id.Btn_addNewProduct_product);
        ly_categoriesContainer = root.findViewById(R.id.Ly_categoriesCheckBox);
        viewCategory = root.findViewById(R.id.mv_categories_product);
        btn_toggleCategory = root.findViewById(R.id.Imgb_toggleCategory_product);
        linearLayoutSubcategoryViews = root.findViewById(R.id.Ly_subcategoriesContainer);
        edt_searchText = root.findViewById(R.id.Edt_searchClient_product);
        tv_messageNotFound = root.findViewById(R.id.Tv_mensajeNotFoundProduct_product);

        btnMoveToAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHMA = new Intent(getContext(), ProductEditActivity.class);
                startActivity(moveHMA);
            }
        });

        for (CategoryResponseWithProducts category : config.getArrCategoriesWithProducts()) {
            CheckBox aux = new CheckBox(getContext());
            aux.setText(category.getName());
            aux.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String selectedCategory = buttonView.getText().toString();

                        ArrayList<String> subcategoriesList = new ArrayList<>();
                        for (CategoryResponseWithProducts item : config.getArrCategoriesWithProducts()){
                            if(Objects.equals(item.getName(), selectedCategory)){
                                subcategoriesList = item.getSubcategories();
                            }
                        }
                        if(subcategoriesList.size()>0){
                            arrayListCategoriesChecked.add(selectedCategory);
                            setNewSubCategory(subcategoriesList,selectedCategory);
                        }
                        

                    } else {
                        String deselectedCategory = buttonView.getText().toString();
                        arrayListCategoriesChecked.remove(deselectedCategory);
                        removeSubCategoryCard(deselectedCategory);
                    }
                    searchProductWithDifferentCategoriesSubcategories();
                }
            });
            ly_categoriesContainer.addView(aux);
        }

        viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSubcategories();
            }
        });

        edt_searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchProductWithDifferentCategoriesSubcategories();
            }
        });
        return root;
    }

    private void toggleSubcategories() {
        if (ly_categoriesContainer.getVisibility() == View.VISIBLE) {
            ly_categoriesContainer.setVisibility(View.GONE);
            btn_toggleCategory.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
        } else {
            ly_categoriesContainer.setVisibility(View.VISIBLE);
            btn_toggleCategory.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
        }
    }
    public void setNewSubCategory(ArrayList<String> subCategory,String category){
        SubcategoryView subcategoryView = new SubcategoryView(getContext());
        subcategoryView.setTitle(category+" (Subcategorías)");
        subcategoryView.setOnSubcategorySelectedListener(new SubcategoryView.OnSubcategorySelectedListener() {
            @Override
            public void onSubcategorySelected(String subcategory, boolean isChecked) {
                searchProductWithDifferentCategoriesSubcategories();
            }
        });
        for (String item : subCategory){
            subcategoryView.addSubcategory(item);
        }
        linearLayoutSubcategoryViews.addView(subcategoryView);
        arrayListSubCategoriesView.add(subcategoryView);
    }
    public void removeSubCategoryCard(String category) {
        for (int i = 0; i < linearLayoutSubcategoryViews.getChildCount(); i++) {
            View view = linearLayoutSubcategoryViews.getChildAt(i);
            if (view instanceof SubcategoryView) {
                SubcategoryView subcategoryView = (SubcategoryView) view;
                if (subcategoryView.getTitle().equals(category + " (Subcategorías)")) {
                    linearLayoutSubcategoryViews.removeView(subcategoryView);
                    arrayListSubCategoriesView.remove(subcategoryView);
                    break;
                }
            }
        }
    }
    private void initProductsData(View root) {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<PagesProductResponse> call = service.getProducts("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesProductResponse> call, @NonNull Response<PagesProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesProductResponse = response.body();
                    if(pagesProductResponse!=null){
                        System.out.println(pagesProductResponse.getDocs().size());
                        productlist  = (ArrayList<ProductResponse>) pagesProductResponse.getDocs();
                        recyclerView.removeAllViews();
                        RecyclerViewAdapterProduct listAdapter = new RecyclerViewAdapterProduct(getContext(), 1, productlist, new RecyclerViewAdapterProduct.OnDetailItem() {
                            @Override
                            public void onClick(ProductResponse product) {
                                Bundle bundle = new Bundle();
                                bundle.putString("product_code",product.getCode());
                                Intent moveHMA = new Intent(getContext(), ProductDetailActivity.class);
                                moveHMA.putExtras(bundle);
                                startActivity(moveHMA);
                            }
                        }, new RecyclerViewAdapterProduct.OnSupplierItem() {
                            @Override
                            public void onClick(ProductResponse product) {

                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(listAdapter);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<PagesProductResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }


    private void searchProductWithDifferentCategoriesSubcategories(){

        String name = edt_searchText.getText().toString();
        String category="";
        String subcategory="";

        ArrayList<String> subcategories = new ArrayList<>();
        for (SubcategoryView item:arrayListSubCategoriesView){
            subcategories.addAll(item.getSelectedSubcategories());
        }
        category = TextUtils.join(",", arrayListCategoriesChecked);

        subcategory = TextUtils.join(",", subcategories);

        if(!Objects.equals(subcategory, ""))category="";

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<PagesProductResponse> call = service.getProductByCategorySubcategorySearch(
                name,
                category,
                subcategory,
                NUMBER_SIZE_PAGINATION,
                "Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesProductResponse> call, @NonNull Response<PagesProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesProductResponse = response.body();
                    if(pagesProductResponse!=null){
                        if(!pagesProductResponse.getDocs().isEmpty()){
                            tv_messageNotFound.setVisibility(View.GONE);
                        }else{
                            tv_messageNotFound.setVisibility(View.VISIBLE);
                        }
                        System.out.println(pagesProductResponse.getDocs().size());
                        productlist  = (ArrayList<ProductResponse>) pagesProductResponse.getDocs();
                        recyclerView.removeAllViews();
                        RecyclerViewAdapterProduct listAdapter = new RecyclerViewAdapterProduct(getContext(), 1, productlist, new RecyclerViewAdapterProduct.OnDetailItem() {
                            @Override
                            public void onClick(ProductResponse product) {
                                Bundle bundle = new Bundle();
                                bundle.putString("product_code",product.getCode());
                                Intent moveHMA = new Intent(getContext(), ProductDetailActivity.class);
                                moveHMA.putExtras(bundle);
                                startActivity(moveHMA);

                            }
                        }, new RecyclerViewAdapterProduct.OnSupplierItem() {
                            @Override
                            public void onClick(ProductResponse product) {

                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(listAdapter);


                    }else{
                        tv_messageNotFound.setVisibility(View.VISIBLE);
                    }

                }else{
                    tv_messageNotFound.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<PagesProductResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
                tv_messageNotFound.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //initProductsData(_root);
        searchProductWithDifferentCategoriesSubcategories();
    }
}