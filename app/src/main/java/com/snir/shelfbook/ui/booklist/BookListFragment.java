package com.snir.shelfbook.ui.booklist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.snir.shelfbook.R;
import com.snir.shelfbook.model.book.Book;
import com.snir.shelfbook.model.book.BookFirebase;
import com.snir.shelfbook.model.book.BookModel;
import com.snir.shelfbook.model.user.User;
import com.snir.shelfbook.model.user.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookListFragment extends Fragment {
    BookListViewModel viewModel;
    RecyclerView list;
    FloatingActionButton addBookBtn;
    MyAdapter adapter;
    SwipeRefreshLayout srl;
    ProgressBar pb;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(BookListViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        viewModel = new ViewModelProvider(this).get(BookListViewModel.class);

        srl = view.findViewById(R.id.BookList_swipeRefreshLayout);

        addBookBtn = getActivity().findViewById(R.id.fab);
        addBookBtn.setVisibility(View.VISIBLE);

        pb = view.findViewById(R.id.BookList_progressBar);

        list = view.findViewById(R.id.BookList_rv);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new MyAdapter();
        list.setAdapter(adapter);
        list.setVisibility(View.INVISIBLE);

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_books_list_to_bookAddFragment);
            }
        });

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                Book book = viewModel.getData().getValue().get(position);

                BookListFragmentDirections.ActionBooksListToBookDetailsFragment action = BookListFragmentDirections.actionBooksListToBookDetailsFragment(book);
                Navigation.findNavController(view).navigate(action);
            }
        });

        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pb.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);   //1 seconds
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                reloadData();
            }
        });

        return view;
    }

    //////////////////////////////////////////////////////////////

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public OnItemClickListener listener;
        TextView giverCity;
        TextView bookName;
        TextView bookCondition;
        ImageView bookImage;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            giverCity = itemView.findViewById(R.id.listRow_giverCityTv);
            bookName = itemView.findViewById(R.id.listRow_nameTv);
            bookCondition = itemView.findViewById(R.id.listRow_condTv);
            bookImage = itemView.findViewById(R.id.listRow_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Book book, int position) {
            BookFirebase.getOwnerData(book.getOwnerId(), new UserModel.Listener<User>() {
                @Override
                public void onComplete(User data) {
                    giverCity.setText(data.getCity());
                }
            });

            bookName.setText(book.getName());
            bookCondition.setText(book.getBookCondition());

            Picasso.get().load(book.getImageUrl()).placeholder(R.drawable.jabbascript).error(R.drawable.jabbascript).into(bookImage);

            this.position = position;
        }
    }

    /////////////////////////////////////////////////////////////

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Book book = viewModel.getData().getValue().get(position);
            holder.bindData(book,position);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null)
                return 0;
            return viewModel.getData().getValue().size();
        }
    }

    ////////////////////////////////////////////////////////

    private void reloadData(){
        addBookBtn.setEnabled(false);
        BookModel.instance.refreshBookList(new BookModel.CompListener() {
            @Override
            public void onComplete() {
                addBookBtn.setEnabled(true);
                srl.setRefreshing(false);
            }
        });
    }
}