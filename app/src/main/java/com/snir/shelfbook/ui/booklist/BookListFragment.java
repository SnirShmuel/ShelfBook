package com.snir.shelfbook.ui.booklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snir.shelfbook.R;
import com.snir.shelfbook.model.Model;
import com.snir.shelfbook.model.book.Book;

import java.util.List;

public class BookListFragment extends Fragment {

    RecyclerView list;
    List<Book> data;

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);


        list = view.findViewById(R.id.BookList_rv);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);

        data = Model.instance.getAllBooks();

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                Book book = data.get(position);

//                BookListFragmentDirections.ActionBooksListToBookDetailsFragment action = BookListFragmentDirections.actionBooksListToBookDetailsFragment();
//                Navigation.findNavController(view).navigate(action);
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
        TextView bookID;
        TextView bookName;
        TextView bookDescription;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookID = itemView.findViewById(R.id.listRow_idTv);
            bookName = itemView.findViewById(R.id.listRow_nameTv);
            bookDescription = itemView.findViewById(R.id.listRow_decTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Book book, int position) {
            bookID.setText(book.getId());
            bookName.setText(book.getName());
            bookDescription.setText(book.getDescription());
            this.position = position;
        }
    }

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
            Book book = data.get(position);
            holder.bindData(book,position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}