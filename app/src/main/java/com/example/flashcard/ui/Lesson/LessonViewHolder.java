package com.example.flashcard.ui.Lesson;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.R;
import com.example.flashcard.data.Lesson;
//import com.example.flashcard.ui.LessonListAdapter;

class LessonViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewName;

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.textViewLessonName);
    }


    public void bind(Lesson lesson, LessonClickListener lis)
    {
        itemView.setTag(lesson);
        textViewName.setText(lesson.getName());
        itemView.setOnClickListener(v -> {
            if (lis != null) {
                lis.onItemClick(lesson);
            }
        });
        itemView.setOnLongClickListener((View.OnLongClickListener) v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_lesson, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Lesson currendLesson = (Lesson) v.getTag();
                    if (currendLesson == null) return false;
                    int id = item.getItemId();
                    if (id == R.id.action_delete)
                    {
                        lis.onDeleteClick(currendLesson);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
            return true;
        });
    }
}