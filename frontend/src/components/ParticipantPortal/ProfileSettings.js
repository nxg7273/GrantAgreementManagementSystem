import React, { useState, useEffect } from 'react';
import { Box, Heading, VStack, FormControl, FormLabel, Input, Switch, Button, useToast } from '@chakra-ui/react';
import axios from 'axios';

const ProfileSettings = () => {
  const [profile, setProfile] = useState({
    name: '',
    email: '',
    organization: '',
    emailNotifications: true,
    inAppNotifications: true,
  });
  const toast = useToast();

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await axios.get('/api/participant/profile');
      setProfile(response.data);
    } catch (error) {
      console.error('Error fetching profile:', error);
      toast({
        title: 'Error',
        description: 'Failed to fetch profile information',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfile({ ...profile, [name]: value });
  };

  const handleSwitchChange = (name) => {
    setProfile({ ...profile, [name]: !profile[name] });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.put('/api/participant/profile', profile);
      toast({
        title: 'Profile Updated',
        description: 'Your profile has been successfully updated',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error updating profile:', error);
      toast({
        title: 'Error',
        description: 'Failed to update profile',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Profile Settings</Heading>
      <form onSubmit={handleSubmit}>
        <VStack spacing={4} align="stretch">
          <FormControl>
            <FormLabel>Name</FormLabel>
            <Input name="name" value={profile.name} onChange={handleInputChange} />
          </FormControl>
          <FormControl>
            <FormLabel>Email</FormLabel>
            <Input name="email" type="email" value={profile.email} onChange={handleInputChange} />
          </FormControl>
          <FormControl>
            <FormLabel>Organization</FormLabel>
            <Input name="organization" value={profile.organization} onChange={handleInputChange} />
          </FormControl>
          <FormControl display="flex" alignItems="center">
            <FormLabel htmlFor="emailNotifications" mb="0">
              Email Notifications
            </FormLabel>
            <Switch
              id="emailNotifications"
              isChecked={profile.emailNotifications}
              onChange={() => handleSwitchChange('emailNotifications')}
            />
          </FormControl>
          <FormControl display="flex" alignItems="center">
            <FormLabel htmlFor="inAppNotifications" mb="0">
              In-App Notifications
            </FormLabel>
            <Switch
              id="inAppNotifications"
              isChecked={profile.inAppNotifications}
              onChange={() => handleSwitchChange('inAppNotifications')}
            />
          </FormControl>
          <Button type="submit" colorScheme="blue">
            Save Changes
          </Button>
        </VStack>
      </form>
    </Box>
  );
};

export default ProfileSettings;
