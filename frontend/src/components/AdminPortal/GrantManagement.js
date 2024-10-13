import React, { useState, useEffect } from 'react';
import { Box, Heading, VStack, HStack, Table, Thead, Tbody, Tr, Th, Td, Button, Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton, FormControl, FormLabel, Input, Textarea, useDisclosure, useToast } from '@chakra-ui/react';
import axios from 'axios';

const GrantManagement = () => {
  const [grants, setGrants] = useState([]);
  const [currentGrant, setCurrentGrant] = useState({ name: '', description: '', amount: '', legalText: '' });
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  useEffect(() => {
    fetchGrants();
  }, []);

  const fetchGrants = async () => {
    try {
      const response = await axios.get('/api/admin/grants');
      setGrants(response.data);
    } catch (error) {
      console.error('Error fetching grants:', error);
      toast({
        title: 'Error',
        description: 'Failed to fetch grants',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentGrant({ ...currentGrant, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (currentGrant.id) {
        await axios.put(`/api/admin/grants/${currentGrant.id}`, currentGrant);
      } else {
        await axios.post('/api/admin/grants', currentGrant);
      }
      onClose();
      fetchGrants();
      toast({
        title: 'Success',
        description: `Grant ${currentGrant.id ? 'updated' : 'created'} successfully`,
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error submitting grant:', error);
      toast({
        title: 'Error',
        description: `Failed to ${currentGrant.id ? 'update' : 'create'} grant`,
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleEdit = (grant) => {
    setCurrentGrant(grant);
    onOpen();
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/admin/grants/${id}`);
      fetchGrants();
      toast({
        title: 'Success',
        description: 'Grant deleted successfully',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error deleting grant:', error);
      toast({
        title: 'Error',
        description: 'Failed to delete grant',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Grant Management</Heading>
      <Button colorScheme="blue" onClick={() => { setCurrentGrant({ name: '', description: '', amount: '', legalText: '' }); onOpen(); }}>
        Create New Grant
      </Button>
      <Table variant="simple" mt={4}>
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Description</Th>
            <Th>Amount</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {grants.map((grant) => (
            <Tr key={grant.id}>
              <Td>{grant.name}</Td>
              <Td>{grant.description}</Td>
              <Td>{grant.amount}</Td>
              <Td>
                <HStack spacing={2}>
                  <Button size="sm" colorScheme="blue" onClick={() => handleEdit(grant)}>Edit</Button>
                  <Button size="sm" colorScheme="red" onClick={() => handleDelete(grant.id)}>Delete</Button>
                </HStack>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>{currentGrant.id ? 'Edit Grant' : 'Create New Grant'}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <VStack spacing={4}>
              <FormControl>
                <FormLabel>Name</FormLabel>
                <Input name="name" value={currentGrant.name} onChange={handleInputChange} />
              </FormControl>
              <FormControl>
                <FormLabel>Description</FormLabel>
                <Textarea name="description" value={currentGrant.description} onChange={handleInputChange} />
              </FormControl>
              <FormControl>
                <FormLabel>Amount</FormLabel>
                <Input name="amount" type="number" value={currentGrant.amount} onChange={handleInputChange} />
              </FormControl>
              <FormControl>
                <FormLabel>Legal Text</FormLabel>
                <Textarea name="legalText" value={currentGrant.legalText} onChange={handleInputChange} />
              </FormControl>
            </VStack>
          </ModalBody>
          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={handleSubmit}>
              {currentGrant.id ? 'Update' : 'Create'}
            </Button>
            <Button onClick={onClose}>Cancel</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
};

export default GrantManagement;
