package com.moneyManagement.service;

import com.moneyManagement.dto.CategoryDTO;
import com.moneyManagement.entity.CategoryEntity;
import com.moneyManagement.entity.ProfileEntity;
import com.moneyManagement.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;


    // Saved Category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())) {
            throw new RuntimeException("Category with this name name already exists");
        }
        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    //Get Category for current user
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    //Get Category by type for current user
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return entities.stream().map(this::toDTO).toList();
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setIcon(categoryDTO.getIcon());
        existingCategory.setType(categoryDTO.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }


    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .type(categoryEntity.getType())
                .build();
    }
}
