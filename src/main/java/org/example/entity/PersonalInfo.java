package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.converter.BirthdayConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    private String firstname;

    private String lastname;

    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}
